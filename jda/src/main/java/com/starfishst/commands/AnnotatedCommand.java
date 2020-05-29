package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.providers.registry.ImplProvidersRegistry;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.ICommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.context.IMappable;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.exceptions.type.SimpleException;
import com.starfishst.core.exceptions.type.SimpleRuntimeException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.cache.Cache;
import com.starfishst.core.utils.time.Time;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.Permission;
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
  @NotNull private final Permission permission;
  /** The list of arguments that the command requires */
  @NotNull private final List<ISimpleArgument<?>> arguments;
  /** The message provider for certain messages */
  @NotNull private final MessagesProvider messagesProvider;
  /** The time to cooldown the use of the message for the users */
  @NotNull private final Time cooldown;
  /**
   * Whether or not if it should be excluded from being deleted it's success check {@link
   * com.starfishst.commands.annotations.Exclude}
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
   * @param cooldown the cooldown of the command
   * @param excluded if the command should be excluded from deleting its success
   */
  public AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      Command cmd,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull MessagesProvider messagesProvider,
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
    this.cooldown = cooldown;
    this.excluded = excluded;
  }

  /**
   * Check the permissions of a command execution
   *
   * @param context the context of the command
   * @return an error result if user is not allowed to use the command else null
   */
  @Nullable
  private Result checkPermissions(@NotNull CommandContext context) {
    if (this.permission != Permission.UNKNOWN && context.getMessage().getMember() != null) {
      if (!context.getMessage().getMember().hasPermission(this.permission)) {
        return new Result(ResultType.PERMISSION, messagesProvider.notAllowed());
      }
    } else if (this.permission != Permission.UNKNOWN && context.getMessage().getMember() == null) {
      return new Result(ResultType.ERROR, messagesProvider.guildOnly());
    }
    return null;
  }

  /**
   * Check the cooldown of the sender
   *
   * @param context the context of the command
   * @return an usage error if the sender is not allowed to use the command yet else null
   */
  @Nullable
  private Result checkCooldown(@NotNull CommandContext context) {
    if (cooldown.millis() != 0) {
      CooldownUser cooldownUser = getCooldownUser(context);
      if (cooldownUser != null) {
        return new Result(ResultType.USAGE, messagesProvider.cooldown(cooldownUser.getTimeLeft()));
      }
    }
    return null;
  }

  /**
   * Get a cooldown user
   *
   * @param context the context to get the sender
   * @return a cooldown user if it exists
   */
  @Nullable
  private CooldownUser getCooldownUser(@NotNull CommandContext context) {
    return (CooldownUser)
        Cache.getCache().stream()
            .filter(
                catchable ->
                    catchable instanceof CooldownUser
                        && context.getSender().getIdLong() == ((CooldownUser) catchable).getId())
            .findFirst()
            .orElse(null);
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
    Result result = checkPermissions(context);
    if (result != null) {
      result = checkCooldown(context);
    }
    if (result != null) {
      return result;
    }
    try {
      Object[] objects = getObjects(context);
      result = (Result) this.method.invoke(this.clazz, objects);
      if (cooldown.millis() != 0) {
        new CooldownUser(cooldown, context.getSender().getIdLong());
      }
      if (result.getSuccess() == null && excluded) {
        result =
            new Result(
                result.getType(), result.getDiscordMessage(), result.getMessage(), message -> {});
      }
      return result;
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
    return ImplProvidersRegistry.getInstance();
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
}
