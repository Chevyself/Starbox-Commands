package me.googas.commands.jda;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.listener.CommandListener;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.permissions.PermissionChecker;
import me.googas.commands.jda.result.Result;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.JDA;

/** The command manager for discord commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The list of registered commands */
  @NonNull @Getter private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The instance of jda where the manager is working */
  @NonNull @Getter private final JDA jda;
  /** The options used for the manager */
  @NonNull @Getter private final ManagerOptions managerOptions;
  /** The provider of messages for the manager */
  @NonNull @Getter private final MessagesProvider messagesProvider;
  /** The listener of command execution */
  @NonNull @Getter private final CommandListener listener;
  /** The providers registry for the commands */
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  /** The permission checker for the commands */
  @NonNull @Getter private final PermissionChecker permissionChecker;

  /**
   * Create an instance
   *
   * @param jda the instance of jda for the manager to function
   * @param prefix the prefix to use in commands
   * @param options the options of the manager
   * @param messagesProvider the provider for messages
   * @param registry the registry that the manager can use
   * @param permissionChecker the permission checker for the commands
   */
  public CommandManager(
      @NonNull JDA jda,
      @NonNull String prefix,
      @NonNull ManagerOptions options,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull PermissionChecker permissionChecker) {
    this.jda = jda;
    this.managerOptions = options;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    this.permissionChecker = permissionChecker;
    this.listener = new CommandListener(prefix, this, options, this.messagesProvider);
    jda.addEventListener(this.listener);
  }

  /**
   * Get a command using its name
   *
   * @param cmd the name of the command
   * @return the command if found or else null
   */
  public AnnotatedCommand getCommand(@NonNull String cmd) {
    for (final AnnotatedCommand command : this.commands) {
      for (final String alias : command.getAliases()) {
        if (alias.equalsIgnoreCase(cmd)) return command;
      }
    }
    return null;
  }

  /**
   * Get a command using a class
   *
   * @param clazz the class to look for
   * @param <O> the class that contains the method
   * @return the command if found else null
   */
  public <O> O getCommand(@NonNull Class<O> clazz) {
    AnnotatedCommand cmd =
        commands.stream()
            .filter(command -> command.getClazz().getClass().equals(clazz))
            .findFirst()
            .orElse(null);
    return cmd == null ? null : clazz.cast(cmd.getClazz());
  }

  @Override
  public void registerCommand(@NonNull Object object) {
    Collection<AnnotatedCommand> commands = parseCommands(object);
    ParentCommand command = null;
    for (AnnotatedCommand annotatedCommand : commands) {
      if (annotatedCommand instanceof ParentCommand) {
        command = (ParentCommand) annotatedCommand;
        this.commands.add(annotatedCommand);
      } else {
        if (command != null) {
          command.addCommand(annotatedCommand);
        } else {
          this.commands.add(annotatedCommand);
        }
      }
    }
  }

  @Override
  public @NonNull Collection<AnnotatedCommand> parseCommands(@NonNull Object object) {
    List<AnnotatedCommand> commands = new ArrayList<>();
    ParentCommand parent = null;
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        parent = (ParentCommand) this.parseCommand(object, method, true);
        commands.add(parent);
        break;
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method, false);
        if (parent != null) {
          parent.addCommand(cmd);
        } else {
          commands.add(cmd);
        }
      }
    }
    return commands;
  }

  @Override
  public ParentCommand getParent(@NonNull String alias) {
    for (AnnotatedCommand command : this.commands) {
      if (command instanceof ParentCommand) {
        if (command.getName().equalsIgnoreCase(alias)) {
          return (ParentCommand) command;
        }
        for (String commandAlias : command.getAliases()) {
          if (commandAlias.equalsIgnoreCase(alias)) {
            return (ParentCommand) command;
          }
        }
      }
    }
    return null;
  }

  @Override
  public void unregister() {
    this.commands.clear();
  }

  public void close(boolean closeJda) {
    commands.clear();
    if (closeJda) {
      jda.shutdown();
    }
  }

  @NonNull
  @Override
  public AnnotatedCommand parseCommand(
      @NonNull Object object, @NonNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class || method.getReturnType().equals(Void.TYPE)) {
      final Class<?>[] params = method.getParameterTypes();
      final Annotation[][] annotations = method.getParameterAnnotations();
      final Command cmd = method.getAnnotation(Command.class);
      final Duration cooldown =
          cmd.time().equalsIgnoreCase("none") ? Duration.ZERO : Duration.parse(cmd.time());
      if (isParent) {
        return new ParentCommand(
            object,
            method,
            cmd,
            parseArguments(params, annotations),
            messagesProvider,
            cooldown,
            registry,
            permissionChecker,
            new HashSet<>(),
            parseSettings(method));
      } else {
        return new AnnotatedCommand(
            object,
            method,
            cmd,
            parseArguments(params, annotations),
            messagesProvider,
            permissionChecker,
            registry,
            cooldown,
            new HashSet<>(),
            parseSettings(method));
      }
    } else {
      throw new CommandRegistrationException(
          "The method of the command must return Result or void @ " + method);
    }
  }
}
