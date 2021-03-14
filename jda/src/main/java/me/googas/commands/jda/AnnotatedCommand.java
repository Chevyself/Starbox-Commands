package me.googas.commands.jda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.ICommand;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.context.Mappable;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.exceptions.type.SimpleException;
import me.googas.commands.exceptions.type.SimpleRuntimeException;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.permissions.PermissionChecker;
import me.googas.commands.jda.permissions.SimplePermission;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.jda.utils.Annotations;
import me.googas.commands.objects.CommandSettings;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.time.Time;
import net.dv8tion.jda.api.entities.User;

/** An annotated command for discord */
public class AnnotatedCommand implements ICommand<CommandContext>, Mappable {

  @NonNull private final Object clazz;
  @NonNull private final Method method;
  @NonNull private final String name;
  @NonNull private final String description;
  @NonNull private final List<String> aliases;
  @NonNull @Getter private final SimplePermission permission;
  @NonNull private final List<ISimpleArgument<?>> arguments;
  @NonNull private final MessagesProvider messagesProvider;
  @NonNull private final PermissionChecker permissionChecker;
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  /** The users that have executed the command */
  @NonNull @Getter private final Set<CooldownUser> cooldownUsers;
  /** The time to cooldown the use of the message for the users */
  @NonNull @Getter @Setter private Time cooldown;

  @NonNull private final CommandSettings settings;

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
   * @param cooldownUsers the list of users that are in cooldown
   * @param settings
   */
  public AnnotatedCommand(
      @NonNull Object clazz,
      @NonNull Method method,
      Command cmd,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull MessagesProvider messagesProvider,
      @NonNull PermissionChecker permissionChecker,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull Time cooldown,
      @NonNull Set<CooldownUser> cooldownUsers,
      @NonNull CommandSettings settings) {
    this.clazz = clazz;
    this.method = method;
    this.name = cmd.aliases()[0];
    this.description = cmd.description();
    this.aliases = Arrays.asList(cmd.aliases());
    this.permission = Annotations.getPermission(cmd);
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.permissionChecker = permissionChecker;
    this.registry = registry;
    this.cooldown = cooldown;
    this.cooldownUsers = cooldownUsers;
    this.settings = settings;
  }

  /**
   * Check the cooldown of the sender
   *
   * @param sender the sender of the command
   * @param context the context of the command
   * @return an usage error if the sender is not allowed to use the command yet else null
   */
  public Result checkCooldown(@NonNull User sender, CommandContext context) {
    if (cooldown.millis() != 0) {
      CooldownUser cooldownUser = getCooldownUser(sender);
      // TODO make them ignore if the user has certain permission
      if (cooldownUser != null && !cooldownUser.isExpired()) {
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
  public CooldownUser getCooldownUser(@NonNull User sender) {
    for (CooldownUser user : this.cooldownUsers) {
      if (user.getId() == sender.getIdLong()) return user;
    }
    return null;
  }

  /**
   * Check whether the context has the permission to execute the command
   *
   * @param context the context to check the command
   * @return true if the context has the permission to execute the command
   */
  public boolean hasPermission(@NonNull CommandContext context) {
    return this.permissionChecker.checkPermission(context, this.permission) == null;
  }

  private boolean isExcluded() {
    return settings.containsFlag("-exclude", true) || settings.containsFlag("exclude", true);
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
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
          this.cooldownUsers.add(new CooldownUser(cooldown, context.getSender().getIdLong()));
        }
        if (result.getSuccess() == null && this.isExcluded()) {
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

  @NonNull
  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @NonNull
  @Override
  public Object getClazz() {
    return clazz;
  }

  @NonNull
  @Override
  public Method getMethod() {
    return method;
  }

  @NonNull
  @Override
  public List<ISimpleArgument<?>> getArguments() {
    return arguments;
  }

  @NonNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NonNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public @NonNull CommandSettings getCommandArguments() {
    return this.settings;
  }

  @Override
  public @NonNull String getName() {
    return name;
  }

  @Override
  public @NonNull String getDescription() {
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
