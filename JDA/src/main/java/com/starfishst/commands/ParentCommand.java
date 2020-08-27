package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.time.Time;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Just like {@link AnnotatedCommand} but has children commands that are executed just like any
 * other with the name of the parent in front
 */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The list of registered commands */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param clazz the class that contains the method
   * @param method the method that invokes the command
   * @param cmd the command name
   * @param arguments the arguments of the command
   * @param messagesProvider the provider of messages
   * @param cooldown the time of cooldown
   * @param excluded if the command should be excluded from deleting its success
   * @param registry the registry to get the providers from
   * @param permissionChecker to check the permission of the command sender
   */
  public ParentCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      Command cmd,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull MessagesProvider messagesProvider,
      @NotNull Time cooldown,
      boolean excluded,
      ProvidersRegistry<CommandContext> registry,
      PermissionChecker permissionChecker) {
    super(
        clazz,
        method,
        cmd,
        arguments,
        messagesProvider,
        permissionChecker,
        registry,
        cooldown,
        excluded);
  }

  @Override
  public @NotNull List<AnnotatedCommand> getCommands() {
    return commands;
  }

  @Nullable
  @Override
  public AnnotatedCommand getCommand(@NotNull String name) {
    for (AnnotatedCommand command : this.commands) {
      if (command.getName().equalsIgnoreCase(name)) {
        return command;
      }
      for (String alias : command.getAliases()) {
        if (alias.equalsIgnoreCase(name)) {
          return command;
        }
      }
    }
    return null;
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
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
