package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.CommandSenderProvider;
import com.starfishst.bungee.providers.ProxiedPlayerProvider;
import com.starfishst.bungee.providers.ProxiedPlayerSenderProvider;
import com.starfishst.bungee.providers.registry.ImplProvidersRegistry;
import com.starfishst.bungee.result.Result;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.BooleanProvider;
import com.starfishst.core.providers.DoubleProvider;
import com.starfishst.core.providers.IntegerProvider;
import com.starfishst.core.providers.JoinedNumberProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.TimeProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
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
    this.addProviders(ImplProvidersRegistry.getInstance(), messagesProvider);
  }

  /**
   * Register the needed providers in the registry
   *
   * @param registry the registry of providers
   * @param messagesProvider the message provider
   */
  private void addProviders(
      @NotNull ProvidersRegistry<CommandContext> registry,
      @NotNull MessagesProvider messagesProvider) {
    registry.addProvider(new BooleanProvider<>(messagesProvider));
    registry.addProvider(new DoubleProvider<>(messagesProvider));
    registry.addProvider(new IntegerProvider<>(messagesProvider));
    registry.addProvider(new JoinedStringsProvider<>());
    registry.addProvider(new LongProvider<>(messagesProvider));
    registry.addProvider(new StringProvider<>());
    registry.addProvider(new TimeProvider<>(messagesProvider));
    registry.addProvider(new JoinedNumberProvider<>(messagesProvider));
    registry.addProvider(new CommandSenderProvider());
    registry.addProvider(new ProxiedPlayerProvider(messagesProvider));
    registry.addProvider(new ProxiedPlayerSenderProvider(messagesProvider));
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

  /**
   * Get if a command is async
   *
   * @param method the method of the command
   * @return true if the command is async
   */
  private boolean isAsync(@NotNull Method method) {
    HashMap<String, String> settings = parseSettings(method);
    return Boolean.parseBoolean(settings.getOrDefault("async", "false"));
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
            messagesProvider,
            plugin,
            isAsync(method));
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider,
            plugin,
            isAsync(method));
      }
    } else {
      throw new CommandRegistrationException("{0} must return {1}");
    }
  }
}
