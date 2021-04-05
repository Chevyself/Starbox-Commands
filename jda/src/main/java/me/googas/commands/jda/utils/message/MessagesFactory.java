package me.googas.commands.jda.utils.message;

import lombok.NonNull;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

/** A factory for creating messages easily */
public class MessagesFactory {

  /** The builder for messages */
  private static final MessageBuilder messageBuilder = new MessageBuilder();

  /**
   * Get a message from a string
   *
   * @param string the string to send as message
   * @return a message query built from a string
   */
  public static Message fromString(@NonNull final String string) {
    return new MessageBuilder().append(string).build();
  }

  /**
   * Get a message query from an embed
   *
   * @param embed the embed to use as message
   * @return the embed as message
   */
  public static Message fromEmbed(@NonNull final MessageEmbed embed) {
    return new MessageBuilder(embed).build();
  }

  /**
   * Get the builder used in the factory. (It is empty)
   *
   * @return the builder
   */
  public static @NonNull MessageBuilder getMessageBuilder() {
    return MessagesFactory.messageBuilder.clear();
  }
}
