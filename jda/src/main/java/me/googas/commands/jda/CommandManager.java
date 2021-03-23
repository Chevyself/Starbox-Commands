package me.googas.commands.jda;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
import me.googas.commands.jda.permissions.PermissionChecker;
import me.googas.commands.jda.result.Result;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.JDA;

public class CommandManager implements EasyCommandManager<CommandContext, EasyJdaCommand> {

  @NonNull @Getter private final List<EasyJdaCommand> commands;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final PermissionChecker permissionChecker;
  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final CommandListener listener;
  @NonNull @Getter private final ManagerOptions managerOptions;

  public CommandManager(
      @NonNull List<EasyJdaCommand> commands,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull PermissionChecker permissionChecker,
      @NonNull JDA jda,
      @NonNull CommandListener listener,
      @NonNull ManagerOptions managerOptions) {
    this.commands = commands;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.permissionChecker = permissionChecker;
    this.jda = jda;
    this.listener = listener;
    this.managerOptions = managerOptions;
  }

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
        || !Result.class.isAssignableFrom(method.getReturnType())
            && !method.getReturnType().equals(Void.TYPE)) {
      throw new IllegalArgumentException(method + " must return void or " + Result.class);
    }
    return new AnnotatedCommand(
        this,
        method.getAnnotation(Command.class),
        method,
        object,
        Argument.parseArguments(method),
        new ArrayList<>());
  }
}
