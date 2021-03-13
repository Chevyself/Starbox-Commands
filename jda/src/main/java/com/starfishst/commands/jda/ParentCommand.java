package com.starfishst.commands.jda;

import com.starfishst.commands.jda.annotations.Command;
import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.messages.MessagesProvider;
import com.starfishst.commands.jda.permissions.PermissionChecker;
import com.starfishst.commands.jda.result.Result;
import me.googas.commands.IParentCommand;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.objects.CommandSettings;
import me.googas.commands.providers.registry.ProvidersRegistry;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import me.googas.commons.Lots;
import me.googas.commons.time.Time;

/**
 * Just like {@link AnnotatedCommand} but has children commands that are executed just like any
 * other with the name of the parent in front
 */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The list of registered commands */
  @NonNull private final List<AnnotatedCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param clazz the class that contains the method
   * @param method the method that invokes the command
   * @param cmd the command name
   * @param arguments the arguments of the command
   * @param messagesProvider the provider of messages
   * @param cooldown the time of cooldown
   * @param registry the registry to get the providers from
   * @param permissionChecker to check the permission of the command sender
   * @param cooldownUsers the collection of users that have executed the command
   * @param settings the parsed settings of the command
   */
  public ParentCommand(
      @NonNull Object clazz,
      @NonNull Method method,
      Command cmd,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull MessagesProvider messagesProvider,
      @NonNull Time cooldown,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull PermissionChecker permissionChecker,
      @NonNull Set<CooldownUser> cooldownUsers,
      @NonNull CommandSettings settings) {
    super(
        clazz,
        method,
        cmd,
        arguments,
        messagesProvider,
        permissionChecker,
        registry,
        cooldown,
        cooldownUsers,
        settings);
  }

  @Override
  public @NonNull List<AnnotatedCommand> getCommands() {
    return commands;
  }

  @Override
  public AnnotatedCommand getCommand(@NonNull String name) {
    for (AnnotatedCommand command : this.commands) {
      if (command.getName().equalsIgnoreCase(name)) return command;
      for (String alias : command.getAliases()) {
        if (alias.equalsIgnoreCase(name)) return command;
      }
    }
    return null;
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    String[] strings = context.getStrings();
    if (strings.length >= 1) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        context.setStrings(Lots.arrayFrom(1, strings));
        return command.execute(context);
      } else {
        return super.execute(context);
      }
    } else {
      return super.execute(context);
    }
  }
}
