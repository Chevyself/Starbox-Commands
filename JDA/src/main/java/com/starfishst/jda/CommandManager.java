package com.starfishst.jda;

import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.jda.annotations.Command;
import com.starfishst.jda.annotations.Exclude;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.listener.CommandListener;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.result.Result;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import me.googas.commons.time.Time;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The command manager for discord commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The list of registered commands */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The instance of jda where the manager is working */
  @NotNull private final JDA jda;
  /** The options used for the manager */
  @NotNull private final ManagerOptions managerOptions;
  /** The provider of messages for the manager */
  @NotNull private final MessagesProvider messagesProvider;
  /** The listener of command execution */
  @NotNull private final CommandListener listener;
  /** The temporal parent command for ticket registering */
  @Nullable private ParentCommand parent;
  /** The providers registry for the commands */
  @NotNull private final ProvidersRegistry<CommandContext> registry;
  /** The permission checker for the commands */
  @NotNull private final PermissionChecker permissionChecker;

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
      @NotNull JDA jda,
      @NotNull String prefix,
      @NotNull ManagerOptions options,
      @NotNull MessagesProvider messagesProvider,
      @NotNull ProvidersRegistry<CommandContext> registry,
      @NotNull PermissionChecker permissionChecker) {
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
  @Nullable
  public AnnotatedCommand getCommand(@NotNull String cmd) {
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
  @Nullable
  public <O> O getCommand(@NotNull Class<O> clazz) {
    AnnotatedCommand cmd =
        commands.stream()
            .filter(command -> command.getClazz().getClass().equals(clazz))
            .findFirst()
            .orElse(null);
    return cmd == null ? null : clazz.cast(cmd.getClazz());
  }

  /**
   * Get the listener that the manager is using
   *
   * @return the listener that the command is using
   */
  @NotNull
  public CommandListener getListener() {
    return listener;
  }

  /**
   * Get the list of registered commands
   *
   * @return the list of registered commands
   */
  @NotNull
  public List<AnnotatedCommand> getCommands() {
    return commands;
  }

  /**
   * Get the messages providers that the command manager is using
   *
   * @return the provider of messages
   */
  @NotNull
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  /**
   * Get the jda instance that the manager is using
   *
   * @return the instance of jda
   */
  @NotNull
  public JDA getJda() {
    return jda;
  }

  /**
   * Get the manager options that the manager is using
   *
   * @return the manager options
   */
  @NotNull
  public ManagerOptions getManagerOptions() {
    return managerOptions;
  }

  @Override
  public void registerCommand(@NotNull Object object) {
    final Class<?> clazz = object.getClass();
    if (clazz != null) {
      for (final Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
          this.parent = (ParentCommand) this.parseCommand(object, method, true);
          this.commands.add(this.parent);
        }
      }
      for (final Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
          final AnnotatedCommand cmd = this.parseCommand(object, method, false);
          if (this.parent != null) {
            this.parent.addCommand(cmd);
          } else {
            this.commands.add(cmd);
          }
        }
      }
      if (this.parent != null) this.parent = null;
    } else {

      throw new CommandRegistrationException(object + " class doesn't exist");
    }
  }

  public void close(boolean closeJda) {
    commands.clear();
    if (closeJda) {
      jda.shutdown();
    }
  }

  /**
   * Get the registry for this command manager
   *
   * @return the registry
   */
  @NotNull
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NotNull
  @Override
  public AnnotatedCommand parseCommand(
      @NotNull Object object, @NotNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class) {
      final Class<?>[] params = method.getParameterTypes();
      final Annotation[][] annotations = method.getParameterAnnotations();
      final Command cmd = method.getAnnotation(Command.class);
      final Time cooldown =
          cmd.time().equalsIgnoreCase("none") ? Time.fromMillis(0) : Time.fromString(cmd.time());
      if (isParent) {
        return new ParentCommand(
            object,
            method,
            cmd,
            parseArguments(params, annotations),
            messagesProvider,
            cooldown,
            hasAnnotation(method.getAnnotations(), Exclude.class),
            registry,
            permissionChecker);
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
            hasAnnotation(method.getAnnotations(), Exclude.class));
      }
    } else {
      throw new CommandRegistrationException(
          "The method of the command must return Result @ " + method);
    }
  }
}
