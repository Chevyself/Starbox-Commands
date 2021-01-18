package com.starfishst.commands.jda.utils.message;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.utils.Chat;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commons.builder.Builder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

/** An easy way to use the messages from {@link MessagesFactory} */
public class MessageQuery implements Builder<Message> {

  /** The builder of the message */
  @NonNull @Delegate @Getter private final MessageBuilder builder;

  /**
   * Create an instance
   *
   * @param builder the builder of the message
   */
  public MessageQuery(@NonNull MessageBuilder builder) {
    this.builder = builder;
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   * @param success the action to execute after the message is send
   */
  public void send(@NonNull MessageChannel channel, Consumer<Message> success) {
    Chat.send(channel, build(), success);
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   */
  public void send(@NonNull MessageChannel channel) {
    Chat.send(channel, build());
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   * @param success the action to execute after the message is send
   */
  public void send(@NonNull CommandContext context, Consumer<Message> success) {
    Chat.send(context, build(), success);
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   */
  public void send(@NonNull CommandContext context) {
    Chat.send(context, build());
  }
}
