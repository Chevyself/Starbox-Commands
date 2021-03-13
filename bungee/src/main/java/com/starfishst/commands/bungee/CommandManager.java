package com.starfishst.commands.bungee;

import com.starfishst.commands.bungee.annotations.Command;
import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.commands.bungee.messages.MessagesProvider;
import com.starfishst.commands.bungee.result.Result;
import me.googas.commands.ICommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.annotations.Nullable;
import me.googas.commons.Strings;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/** The command manager for bungee commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The plugin that is running the manager */
  @NonNull @Getter private final Plugin plugin;
  /** The plugin manager for registering commands */
  @NonNull @Getter private final PluginManager manager;
  /** The messages provider */
  @NonNull private final MessagesProvider messagesProvider;
  /** The registry for the commands */
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  /** THe list of registered commands */
  @NonNull @Getter private final List<AnnotatedCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param plugin the plugin that will create the commands
   * @param messagesProvider the messages provider
   * @param registry the registry for commands
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    this.plugin = plugin;
    this.manager = plugin.getProxy().getPluginManager();
    this.messagesProvider = messagesProvider;
    this.registry = registry;
  }

  @Override
  public void registerCommand(@NonNull Object object) {
    Collection<AnnotatedCommand> commands = parseCommands(object);
    ParentCommand command = null;
    for (AnnotatedCommand annotatedCommand : commands) {
      if (annotatedCommand instanceof ParentCommand) {
        command = (ParentCommand) annotatedCommand;
        this.manager.registerCommand(this.plugin, annotatedCommand);
        this.commands.add(annotatedCommand);
      } else {
        if (command != null) {
          command.addCommand(annotatedCommand);
        } else {
          this.manager.registerCommand(this.plugin, annotatedCommand);
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

  @Nullable
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
    for (AnnotatedCommand command : this.commands) {
      manager.unregisterCommand(command);
    }
    this.commands.clear();
  }

  @NonNull
  @Override
  public AnnotatedCommand parseCommand(
      @NonNull Object object, @NonNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class || method.getReturnType().equals(Void.TYPE)) {
      Annotation[][] annotations = method.getParameterAnnotations();
      Class<?>[] parameters = method.getParameterTypes();
      Command command = method.getAnnotation(Command.class);
      if (isParent) {
        return new ParentCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider,
            plugin,
            registry,
            this.parseSettings(method));
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider,
            plugin,
            registry,
            this.parseSettings(method));
      }
    } else {
      throw new CommandRegistrationException(
          Strings.build("{0} must return {1} or void", method, Result.class));
    }
  }
}
