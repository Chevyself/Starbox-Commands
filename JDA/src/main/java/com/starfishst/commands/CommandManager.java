package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.annotations.Exclude;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.listener.CommandListener;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.providers.CommandContextProvider;
import com.starfishst.commands.providers.GuildCommandContextProvider;
import com.starfishst.commands.providers.GuildProvider;
import com.starfishst.commands.providers.MemberProvider;
import com.starfishst.commands.providers.MemberSenderProvider;
import com.starfishst.commands.providers.MessageProvider;
import com.starfishst.commands.providers.RoleProvider;
import com.starfishst.commands.providers.TextChannelExtraProvider;
import com.starfishst.commands.providers.TextChannelProvider;
import com.starfishst.commands.providers.UserProvider;
import com.starfishst.commands.providers.registry.ImplProvidersRegistry;
import com.starfishst.commands.result.Result;
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
import com.starfishst.core.utils.time.Time;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Create an instance
   *
   * @param jda the instance of jda for the manager to function
   * @param prefix the prefix to use in commands
   * @param options the options of the manager
   * @param messagesProvider the provider for messages
   */
  public CommandManager(
      @NotNull JDA jda,
      @NotNull String prefix,
      @NotNull ManagerOptions options,
      @NotNull MessagesProvider messagesProvider) {
    this.jda = jda;
    this.managerOptions = options;
    this.messagesProvider = messagesProvider;
    this.listener = new CommandListener(prefix, this, options, this.messagesProvider);
    jda.addEventListener(this.listener);
    addProviders(ImplProvidersRegistry.getInstance(), this.messagesProvider);
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
    registry.addProvider(new CommandContextProvider());
    registry.addProvider(new GuildCommandContextProvider(messagesProvider));
    registry.addProvider(new GuildProvider(messagesProvider));
    registry.addProvider(new MemberProvider(messagesProvider));
    registry.addProvider(new MemberSenderProvider(messagesProvider));
    registry.addProvider(new MessageProvider());
    registry.addProvider(new RoleProvider(messagesProvider));
    registry.addProvider(new TextChannelExtraProvider());
    registry.addProvider(new TextChannelProvider(messagesProvider));
    registry.addProvider(new UserProvider(messagesProvider));
    registry.addProvider(new JoinedNumberProvider<>(messagesProvider));
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
    return clazz.cast(
        commands.stream().filter(command -> command.getClazz() == clazz).findFirst().orElse(null));
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
            hasAnnotation(method.getAnnotations(), Exclude.class));
      } else {
        return new AnnotatedCommand(
            object,
            method,
            cmd,
            parseArguments(params, annotations),
            messagesProvider,
            cooldown,
            hasAnnotation(method.getAnnotations(), Exclude.class));
      }
    } else {
      throw new CommandRegistrationException(
          "The method of the command must return Result @ " + method);
    }
  }
}
