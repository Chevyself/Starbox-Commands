package com.starfishst.commands.bungee;

import com.starfishst.commands.bungee.annotations.Command;
import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.commands.bungee.messages.MessagesProvider;
import com.starfishst.commands.bungee.result.Result;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;
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
  /** The temporal command for registering commands there and not in the manager */
  private ParentCommand parent;

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
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        this.parent = (ParentCommand) this.parseCommand(object, method, true);
        this.manager.registerCommand(this.plugin, this.parent);
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method, false);
        if (this.parent != null) {
          this.parent.addCommand(cmd);
        } else {
          this.manager.registerCommand(this.plugin, cmd);
        }
      }
    }
    this.parent = null;
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
