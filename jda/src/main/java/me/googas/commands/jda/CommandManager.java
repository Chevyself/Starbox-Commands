package me.googas.commands.jda;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.arguments.Argument;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.listener.CommandListener;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.permissions.EasyPermission;
import me.googas.commands.jda.permissions.PermissionChecker;
import me.googas.commands.jda.result.Result;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.EasyContextualProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The command that are registered inside this manager makes them work in the {@link #listener} this
 * means that the {@link CommandListener} will execute the command when the bot receives a message
 * {@link CommandListener#onMessageReceivedEvent(MessageReceivedEvent)}
 *
 * <p>The easiest way to create commands is using reflection with the method {@link
 * #parseCommands(Object)} those parsed commands can be later registered in this instance using
 * {@link #registerAll(Collection)}.
 *
 * <p>To create a {@link CommandManager} instance you simply need a {@link ProvidersRegistry} you
 * can use {@link me.googas.commands.jda.providers.registry.JdaProvidersRegistry} which includes
 * some providers that are intended for JDA use you can even extend it to add more in the
 * constructor or use {@link ProvidersRegistry#addProvider(EasyContextualProvider)}, you also ned a
 * {@link MessagesProvider} which is used to display error commands the commands the default
 * implementation is {@link me.googas.commands.jda.messages.JdaMessagesProvider}, to check the
 * permissions of the {@link net.dv8tion.jda.api.entities.User} that execute the command you can
 * create an implementation of {@link PermissionChecker} or just use its default method {@link
 * PermissionChecker#checkPermission(CommandContext, EasyPermission)} which only checks for {@link
 * net.dv8tion.jda.api.Permission}, the instance of {@link JDA} is required to register the {@link
 * CommandListener}, the {@link ListenerOptions} changes some of the logic inside {@link
 * CommandListener} and finally the prefix is the {@link String} that must contain the message at
 * the start
 *
 * <pre>{@code
 * JDA jda = ...
 * MessagesProvider messagesProvider = ...
 * new CommandManager(new JdaProvidersRegistry(messagesProvider), messagesProvider, () -&gt; messagesProvider, jda, new ListenerOptions(), "-");
 * }</pre>
 */
public class CommandManager implements EasyCommandManager<CommandContext, EasyJdaCommand> {

  @NonNull @Getter private final List<EasyJdaCommand> commands = new ArrayList<>();
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final PermissionChecker permissionChecker;
  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final ListenerOptions listenerOptions;
  @NonNull @Getter private final CommandListener listener;

  /**
   * Create an instance
   *
   * @param providersRegistry the providers registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link CommandContext}
   * @param messagesProvider the messages provider for important messages
   * @param permissionChecker to check the permissions of {@link net.dv8tion.jda.api.entities.User}
   *     upon command execution
   * @param jda the instance to register the {@link #listener} on
   * @param listenerOptions to change some of the login in the {@link #listener}
   * @param prefix the prefix that message must have to execute commands
   */
  public CommandManager(
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull PermissionChecker permissionChecker,
      @NonNull JDA jda,
      @NonNull ListenerOptions listenerOptions,
      @NonNull String prefix) {
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.permissionChecker = permissionChecker;
    this.jda = jda;
    this.listenerOptions = listenerOptions;
    this.listener = new CommandListener(prefix, this, listenerOptions, messagesProvider);
    jda.addEventListener(listener);
  }

  /**
   * Get the command instance that matches the name. This will loop thru all the {@link #commands}
   * until one is {@link EasyJdaCommand#hasAlias(String)} = true.
   *
   * @param name the name to match the command
   * @return the instance of the command if found else null
   */
  public EasyJdaCommand getCommand(@NonNull String name) {
    for (EasyJdaCommand command : this.commands) {
      if (command.hasAlias(name)) return command;
    }
    return null;
  }

  @Override
  public @NonNull CommandManager register(@NonNull EasyJdaCommand command) {
    commands.add(command);
    return this;
  }

  @Override
  public @NonNull List<AnnotatedCommand> parseCommands(@NonNull Object object) {
    List<AnnotatedCommand> commands = new ArrayList<>();
    AnnotatedCommand parent = null;
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        parent = this.parseCommand(object, method);
        commands.add(parent);
        break;
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (!method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method);
        if (parent != null) {
          parent.addChildren(cmd);
        } else {
          commands.add(cmd);
        }
      }
    }
    return commands;
  }

  @Override
  public @NonNull AnnotatedCommand parseCommand(@NonNull Object object, @NonNull Method method) {
    if (!Result.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new IllegalArgumentException(method + " must return void or " + Result.class);
    }
    return new AnnotatedCommand(
        this, method.getAnnotation(Command.class), method, object, Argument.parseArguments(method));
  }

  @Override
  public @NonNull CommandManager register(@NonNull Object object) {
    return (CommandManager) EasyCommandManager.super.register(object);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull Object... objects) {
    return (CommandManager) EasyCommandManager.super.register(objects);
  }

  @Override
  public @NonNull CommandManager registerAll(
      @NonNull Collection<? extends EasyJdaCommand> commands) {
    return (CommandManager) EasyCommandManager.super.register(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull EasyJdaCommand... commands) {
    return (CommandManager) EasyCommandManager.super.register(commands);
  }
}
