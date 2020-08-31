package com.starfishst.commands.utils.message;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.utils.Chat;
import java.util.function.Consumer;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An easy way to use the messages from {@link MessagesFactory} */
public class MessageQuery {

  /** The builder of the message */
  @NotNull private final MessageBuilder builder;

  /**
   * Create an instance
   *
   * @param builder the builder of the message
   */
  public MessageQuery(@NotNull MessageBuilder builder) {
    this.builder = builder;
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   * @param success the action to execute after the message is send
   */
  public void send(@NotNull MessageChannel channel, @Nullable Consumer<Message> success) {
    Chat.send(channel, getMessage(), success);
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   */
  public void send(@NotNull MessageChannel channel) {
    Chat.send(channel, getMessage());
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   * @param success the action to execute after the message is send
   */
  public void send(@NotNull CommandContext context, @Nullable Consumer<Message> success) {
    Chat.send(context, getMessage(), success);
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   */
  public void send(@NotNull CommandContext context) {
    Chat.send(context, getMessage());
  }

  /**
   * Get the builder of the query
   *
   * @return the message builder of the query
   */
  @NotNull
  public MessageBuilder getBuilder() {
    return builder;
  }

  /**
   * Get the built message using the builder of the query
   *
   * @return the built message
   */
  @NotNull
  public Message getMessage() {
    return builder.build();
  }
}
