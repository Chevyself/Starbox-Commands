package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.result.Result;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The command manager for bungee commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The plugin that is running the manager */
  @NotNull private final Plugin plugin;
  /** The plugin manager for registering commands */
  @NotNull private final PluginManager manager;
  /** The messages provider */
  @NotNull private final MessagesProvider messagesProvider;
  /** The temporal command for registering commands there and not in the manager */
  @Nullable private ParentCommand parent;

  /**
   * Create an instance
   *
   * @param plugin the plugin that will create the commands
   * @param messagesProvider the messages provider
   */
  public CommandManager(@NotNull Plugin plugin, @NotNull MessagesProvider messagesProvider) {
    this.plugin = plugin;
    this.manager = plugin.getProxy().getPluginManager();
    this.messagesProvider = messagesProvider;
  }

  @Override
  public void registerCommand(@NotNull Object object) {
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

  @NotNull
  @Override
  public AnnotatedCommand parseCommand(
      @NotNull Object object, @NotNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class) {
      Annotation[][] annotations = method.getParameterAnnotations();
      Class<?>[] parameters = method.getParameterTypes();
      Command command = method.getAnnotation(Command.class);

      if (isParent) {
        return new ParentCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider);
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider);
      }
    } else {
      throw new CommandRegistrationException("{0} must return {1}");
    }
  }
}
