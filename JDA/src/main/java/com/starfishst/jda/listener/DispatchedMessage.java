package com.starfishst.jda.listener;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A message of a command that was dispatched and not actually send */
public class DispatchedMessage extends DataMessage {

  /** The message where the message was dispatched */
  @NotNull private final MessageChannel channel;
  /** The author of the dispatched message */
  @NotNull private final User author;
  /** The author as a member of the dispatched message */
  @Nullable private final Member member;

  /**
   * Create a dispatched message
   *
   * @param build the message that requires the author
   * @param channel the channel where the message was dispatched
   * @param author the author of the message
   * @param member the author as a message
   */
  public DispatchedMessage(
      @NotNull Message build,
      @NotNull MessageChannel channel,
      @NotNull User author,
      @Nullable Member member) {
    super(
        build.isTTS(),
        build.getContentRaw(),
        build.getNonce(),
        build.getEmbeds().isEmpty() ? null : build.getEmbeds().get(0));
    this.channel = channel;
    this.author = author;
    this.member = member;
  }

  /**
   * Create a dispatched message
   *
   * @param build the message that requires the author
   * @param channel the channel where the message was dispatched
   * @param author the author of the message
   */
  public DispatchedMessage(
      @NotNull Message build, @NotNull MessageChannel channel, @NotNull User author) {
    this(build, channel, author, null);
  }

  /**
   * Create a dispatched message
   *
   * @param build the message that requires the author
   * @param channel the channel where the message was dispatched
   * @param author the author of the message as a member
   */
  public DispatchedMessage(
      @NotNull Message build, @NotNull MessageChannel channel, @NotNull Member author) {
    this(build, channel, author.getUser(), author);
  }

  @NotNull
  @Override
  public User getAuthor() {
    return author;
  }

  @Nullable
  @Override
  public Member getMember() {
    return member;
  }

  @NotNull
  @Override
  public Guild getGuild() {
    if (channel instanceof TextChannel) {
      return ((TextChannel) channel).getGuild();
    } else {
      throw new UnsupportedOperationException(
          "You cannot get a guild from a message that was not executed from one!");
    }
  }

  @Override
  public boolean isFromGuild() {
    return channel instanceof TextChannel;
  }

  @Nonnull
  @Override
  public ChannelType getChannelType() {
    return channel.getType();
  }
}
