package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.result.Result;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Parent command for bungee commands */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The list of children commands */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param object the object that contains the method
   * @param method the method to invoke
   * @param arguments the arguments to get the parameters for the method to invoke
   * @param command the annotations to get the parameters of the command
   * @param messagesProvider the messages provider
   */
  public ParentCommand(
      Object object,
      Method method,
      List<ISimpleArgument<?>> arguments,
      Command command,
      MessagesProvider messagesProvider) {
    super(object, method, arguments, command, messagesProvider);
  }

  @Override
  public @NotNull List<AnnotatedCommand> getCommands() {
    return commands;
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
    String[] strings = context.getStrings();
    if (strings.length >= 1) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.execute(
            new CommandContext(
                context.getSender(),
                Arrays.copyOfRange(strings, 1, strings.length),
                messagesProvider));
      } else {
        return super.execute(context);
      }
    } else {
      return super.execute(context);
    }
  }

  @Nullable
  @Override
  public AnnotatedCommand getCommand(@NotNull String name) {
    return this.commands.stream()
        .filter(
            command -> {
              if (command.getName().equalsIgnoreCase(name)) {
                return true;
              } else {
                for (String alias : command.getAliases()) {
                  if (alias.equalsIgnoreCase(name)) {
                    return true;
                  }
                }
              }
              return false;
            })
        .findFirst()
        .orElse(null);
  }

  @Override
  public void addCommand(@NotNull AnnotatedCommand command) {
    this.commands.add(command);
  }
}
