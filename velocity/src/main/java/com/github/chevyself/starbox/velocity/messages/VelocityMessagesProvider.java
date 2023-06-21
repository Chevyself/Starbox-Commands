package com.github.chevyself.starbox.velocity.messages;

import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

public interface VelocityMessagesProvider extends MessagesProvider<CommandContext> {
  /**
   * The message sent when a command is executed by another entity rather than a player.
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String onlyPlayers(@NonNull CommandContext context);

  /**
   * The message sent when a player is not found.
   *
   * @param string the input string querying for a player name
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String invalidPlayer(@NonNull String string, @NonNull CommandContext context);
}
