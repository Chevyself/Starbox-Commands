package com.starfishst.jda;

import com.starfishst.core.ICommand;
import com.starfishst.core.arguments.ISimpleArgument;
import com.starfishst.core.context.IMappable;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.exceptions.type.SimpleException;
import com.starfishst.core.exceptions.type.SimpleRuntimeException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.jda.annotations.Command;
import com.starfishst.jda.annotations.Perm;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.result.Result;
import com.starfishst.jda.result.ResultType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import me.googas.commons.cache.thread.Cache;
import me.googas.commons.time.Time;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An annotated command for discord */
public class AnnotatedCommand implements ICommand<CommandContext>, IMappable {

  /** The class that contains the method that executes the command */
  @NotNull private final Object clazz;
  /** The method that executes the command */
  @NotNull private final Method method;
  /** The name of the command */
  @NotNull private final String name;
  /** The description of the command */
  @NotNull private final String description;
  /** Other aliases of the command */
  @NotNull private final List<String> aliases;
  /** The permission required to execute the command */
  @NotNull private final Perm permission;
  /** The list of arguments that the command requires */
  @NotNull private final List<ISimpleArgument<?>> arguments;
  /** The message provider for certain messages */
  @NotNull private final MessagesProvider messagesProvider;
  /** The permission checker for the permissions of the command sender */
  @NotNull private final PermissionChecker permissionChecker;

  @NotNull private final ProvidersRegistry<CommandContext> registry;
  /** The time to cooldown the use of the message for the users */
  @NotNull private Time cooldown;
  /**
   * Whether or not if it should be excluded from being deleted it's success check {@link
   * com.starfishst.jda.annotations.Exclude}
   */
  private final boolean excluded;

  /**
   * Create an instance
   *
   * @param clazz the class where the command is located
   * @param method the annotated method that represents the command
   * @param cmd the annotation of the method
   * @param arguments the arguments of the command
   * @param messagesProvider the provider of messages for the command
   * @param permissionChecker the permissions checker to check the command sender
   * @param registry the registry to get the providers from
   * @param cooldown the cooldown of the command
   * @param excluded if the command should be excluded from deleting its success
   */
  public AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      Command cmd,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull MessagesProvider messagesProvider,
      @NotNull PermissionChecker permissionChecker,
      @NotNull ProvidersRegistry<CommandContext> registry,
      @NotNull Time cooldown,
      boolean excluded) {
    this.clazz = clazz;
    this.method = method;
    this.name = cmd.aliases()[0];
    this.description = cmd.description();
    this.aliases = Arrays.asList(cmd.aliases());
    this.permission = cmd.permission();
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.permissionChecker = permissionChecker;
    this.registry = registry;
    this.cooldown = cooldown;
    this.excluded = excluded;
  }

  /**
   * Check the cooldown of the sender
   *
   * @param sender the sender of the command
   * @param context the context of the command
   * @return an usage error if the sender is not allowed to use the command yet else null
   */
  @Nullable
  public Result checkCooldown(@NotNull User sender, @Nullable CommandContext context) {
    if (cooldown.millis() != 0) {
      CooldownUser cooldownUser = getCooldownUser(sender);
      if (cooldownUser != null) {
        return new Result(
            ResultType.USAGE, messagesProvider.cooldown(cooldownUser.getTimeLeft(), context));
      }
    }
    return null;
  }

  /**
   * Get a cooldown user
   *
   * @param sender the sender to check the cooldown
   * @return a cooldown user if it exists
   */
  @Nullable
  public CooldownUser getCooldownUser(@NotNull User sender) {
    return Cache.getNotRefresh(
        CooldownUser.class,
        user -> user.getCommand() == this && user.getId() == sender.getIdLong());
  }

  /**
   * Set the cooldown of the command
   *
   * @param cooldown the new cooldown
   */
  public void setCooldown(@NotNull Time cooldown) {
    this.cooldown = cooldown;
  }

  /**
   * Get the cooldown of the command
   *
   * @return the cooldown
   */
  @NotNull
  public Time getCooldown() {
    return cooldown;
  }

  /**
   * Get the permission of the command
   *
   * @return the permission of the command
   */
  @NotNull
  public Perm getPermission() {
    return permission;
  }

  /**
   * Check whether the context has the permission to execute the command
   *
   * @param context the context to check the command
   * @return true if the context has the permission to execute the command
   */
  public boolean hasPermission(@NotNull CommandContext context) {
    return this.permissionChecker.checkPermission(context, this.permission) == null;
  }

  @Override
  public @Nullable Result execute(@NotNull CommandContext context) {
    Result result = this.permissionChecker.checkPermission(context, this.permission);
    if (result != null) {
      return result;
    }
    result = checkCooldown(context.getSender(), context);
    if (result != null) {
      return result;
    }
    try {
      Object[] objects = getObjects(context);
      Object object = this.method.invoke(this.clazz, objects);
      if (object instanceof Result) {
        result = (Result) object;
        if (cooldown.millis() != 0) {
          new CooldownUser(cooldown, this, context.getSender().getIdLong());
        }
        if (result.getSuccess() == null && excluded) {
          result =
              new Result(
                  result.getType(), result.getDiscordMessage(), result.getMessage(), message -> {});
        }
        return result;
      } else {
        return null;
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result(ResultType.UNKNOWN, "IllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getTargetException().getMessage();
      if (message != null && !message.isEmpty()) {
        if (!(e.getTargetException() instanceof SimpleException)
            | !(e.getTargetException() instanceof SimpleRuntimeException)) {
          e.printStackTrace();
        }
        return new Result(ResultType.ERROR, message);
      } else {
        e.printStackTrace();
        return new Result(ResultType.UNKNOWN, "InvocationTargetException, e");
      }
    } catch (MissingArgumentException e) {
      return new Result(ResultType.USAGE, e.getMessage());
    } catch (ArgumentProviderException e) {
      return new Result(ResultType.ERROR, e.getMessage());
    }
  }

  @NotNull
  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @NotNull
  @Override
  public Object getClazz() {
    return clazz;
  }

  @NotNull
  @Override
  public Method getMethod() {
    return method;
  }

  @NotNull
  @Override
  public List<ISimpleArgument<?>> getArguments() {
    return arguments;
  }

  @NotNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NotNull
  @Override
  public IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof AnnotatedCommand)) return false;

    AnnotatedCommand command = (AnnotatedCommand) object;

    if (!clazz.equals(command.clazz)) return false;
    if (!method.equals(command.method)) return false;
    if (!name.equals(command.name)) return false;
    if (!aliases.equals(command.aliases)) return false;
    return permission == command.permission;
  }

  @Override
  public int hashCode() {
    int result = clazz.hashCode();
    result = 31 * result + method.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + aliases.hashCode();
    result = 31 * result + permission.hashCode();
    return result;
  }
}
