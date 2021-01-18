package com.starfishst.commands.jda.utils;

import com.starfishst.commands.jda.context.CommandContext;
import java.util.function.Consumer;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

/** Utils for sending messages in discord */
public class Chat {

  /**
   * Sends a message and adds the option of a callback
   *
   * @param channel the channel to send the message
   * @param message the message to send
   * @param success the callback
   */
  public static void send(
      @NonNull final MessageChannel channel,
      @NonNull final Message message,
      final Consumer<Message> success) {
    if (success != null) {
      channel.sendMessage(message).queue(success);
    } else {
      channel.sendMessage(message).queue();
    }
  }

  /**
   * Sends a message to a channel
   *
   * @param channel the channel to send the message
   * @param message the message to send
   */
  public static void send(@NonNull final MessageChannel channel, @NonNull final Message message) {
    Chat.send(channel, message, null);
  }

  /**
   * Send a message using the channel of a 'context' and provide the option of a callback
   *
   * @param context the context to send the message
   * @param message the message to send
   * @param success the callback of the message
   */
  public static void send(
      @NonNull final CommandContext context,
      @NonNull final Message message,
      final Consumer<Message> success) {
    Chat.send(context.getChannel(), message, success);
  }

  /**
   * Send a message using the channel of a 'context'
   *
   * @param context the context of the message
   * @param message the message to send
   */
  public static void send(@NonNull final CommandContext context, @NonNull final Message message) {
    Chat.send(context, message, null);
  }
}
