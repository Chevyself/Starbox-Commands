package com.starfishst.commands.utils;

import com.starfishst.commands.context.CommandContext;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull final MessageChannel channel,
      @NotNull final Message message,
      @Nullable final Consumer<Message> success) {
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
  public static void send(@NotNull final MessageChannel channel, @NotNull final Message message) {
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
      @NotNull final CommandContext context,
      @NotNull final Message message,
      @Nullable final Consumer<Message> success) {
    Chat.send(context.getChannel(), message, success);
  }

  /**
   * Send a message using the channel of a 'context'
   *
   * @param context the context of the message
   * @param message the message to send
   */
  public static void send(@NotNull final CommandContext context, @NotNull final Message message) {
    Chat.send(context, message, null);
  }
}
