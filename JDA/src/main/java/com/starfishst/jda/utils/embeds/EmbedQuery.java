package com.starfishst.jda.utils.embeds;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.utils.Chat;
import com.starfishst.jda.utils.message.MessageQuery;
import com.starfishst.jda.utils.message.MessagesFactory;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An easy way to send embeds */
public class EmbedQuery {

  /** The builder of the embed */
  @NotNull private final EmbedBuilder embed;

  /**
   * Create an instance
   *
   * @param embed the builder of the embed
   */
  public EmbedQuery(@NotNull EmbedBuilder embed) {
    this.embed = embed;
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   * @param success the action to execute after the message is send
   */
  public void send(@NotNull MessageChannel channel, @Nullable Consumer<Message> success) {
    Chat.send(channel, this.getAsMessageQuery().getMessage(), success);
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   */
  public void send(@NotNull MessageChannel channel) {
    Chat.send(channel, this.getAsMessageQuery().getMessage());
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   * @param success the action to execute after the message is send
   */
  public void send(@NotNull CommandContext context, @Nullable Consumer<Message> success) {
    Chat.send(context, this.getAsMessageQuery().getMessage(), success);
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   */
  public void send(@NotNull CommandContext context) {
    Chat.send(context, this.getAsMessageQuery().getMessage());
  }

  /**
   * Get the embed
   *
   * @return the embed
   */
  @NotNull
  public MessageEmbed getEmbed() {
    return embed.build();
  }

  /**
   * Get as a message query
   *
   * @return the message query
   */
  @NotNull
  public MessageQuery getAsMessageQuery() {
    return MessagesFactory.fromEmbed(getEmbed());
  }

  /**
   * Get the embed builder
   *
   * @return the embed builder
   */
  @NotNull
  public EmbedBuilder getEmbedBuilder() {
    return embed;
  }
}
