package com.starfishst.commands.messages;

import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.ResultType;
import org.jetbrains.annotations.NotNull;

public interface MessagesProvider {

  /**
   * @return The message when a command is not found in {@link
   * com.starfishst.commands.CommandManager}
   */
  @NotNull
  String commandNotFound();

  /**
   * @return The footer in case {@link ManagerOptions#isEmbedMessages()} is true
   */
  @NotNull
  String footer();

  /**
   * @param type the type of result
   * @return the title to use for a result
   */
  @NotNull
  String getTitle(@NotNull ResultType type);

  /**
   * {0} is the title from the {@link ResultType} that you get from {@link #getTitle(ResultType)}
   * {1} is the result that you get from {@link
   * com.starfishst.commands.AnnotatedCommand#execute(CommandContext)}
   *
   * @return the message when the result has a message
   */
  @NotNull
  String response();

  /**
   * @return the message when the sender does not have a permission
   */
  @NotNull
  String notAllowed();

  /**
   * @return the message when the command has to be executed in a {@link
   * net.dv8tion.jda.api.entities.Guild}
   */
  @NotNull
  String guildOnly();

  /**
   * {0} is the name of the argument {1} is the description of the argument {2} is the position
   * where the argument is missing
   *
   * @return The error when the message is missing arguments
   */
  @NotNull
  String missingArgument();
}
