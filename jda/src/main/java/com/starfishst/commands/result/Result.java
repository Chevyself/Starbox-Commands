package com.starfishst.commands.result;

import com.starfishst.commands.utils.embeds.EmbedQuery;
import com.starfishst.commands.utils.message.MessageQuery;
import com.starfishst.core.result.IResult;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This is your general type of result */
public class Result implements IResult {

  /** The type of the result */
  @NotNull private final ResultType type;
  /** The message send as a result. The discord message will be send over the message string */
  @Nullable private final Message discordMessage;
  /** A more simple message to send as a result */
  @Nullable private final String message;
  /** The action to do with the message after it is sent */
  @Nullable private final Consumer<Message> success;

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param discordMessage the discord message to send
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(
      @NotNull ResultType type,
      @Nullable Message discordMessage,
      @Nullable String message,
      @Nullable Consumer<Message> success) {
    this.type = type;
    this.discordMessage = discordMessage;
    this.message = message;
    this.success = success;
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param discordMessage the discord message to send
   * @param success the action to do after the message
   */
  public Result(
      @NotNull ResultType type,
      @NotNull Message discordMessage,
      @Nullable Consumer<Message> success) {
    this(type, discordMessage, null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param discordMessage the discord message to send
   */
  public Result(@NotNull ResultType type, @NotNull Message discordMessage) {
    this(type, discordMessage, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(
      @NotNull ResultType type, @NotNull String message, @Nullable Consumer<Message> success) {
    this(type, null, message, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param message the message to send
   */
  public Result(@NotNull ResultType type, @NotNull String message) {
    this(type, message, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the message query to send
   * @param success the action to do after the message
   */
  public Result(
      @NotNull ResultType type, @NotNull MessageQuery query, @Nullable Consumer<Message> success) {
    this(type, query.getMessage(), null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the message query to send
   */
  public Result(@NotNull ResultType type, @NotNull MessageQuery query) {
    this(type, query, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the embed query to send
   * @param success the action to do after the message
   */
  public Result(
      @NotNull ResultType type, @NotNull EmbedQuery query, @Nullable Consumer<Message> success) {
    this(type, query.getAsMessageQuery().getMessage(), null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the embed query to send
   */
  public Result(@NotNull ResultType type, @NotNull EmbedQuery query) {
    this(type, query.getAsMessageQuery().getMessage(), null);
  }

  /**
   * Create an instance with generic result type
   *
   * @param discordMessage the message to send
   * @param success the action to do after the message
   */
  public Result(@NotNull Message discordMessage, @Nullable Consumer<Message> success) {
    this(ResultType.GENERIC, discordMessage, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param discordMessage the message to send
   */
  public Result(@NotNull Message discordMessage) {
    this(ResultType.GENERIC, discordMessage);
  }

  /**
   * Create an instance with generic result type
   *
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(@NotNull String message, @Nullable Consumer<Message> success) {
    this(ResultType.GENERIC, message, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param message the message to send
   */
  public Result(@NotNull String message) {
    this(ResultType.GENERIC, message);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the message query to send
   * @param success the action to do after the message
   */
  public Result(@NotNull MessageQuery query, @Nullable Consumer<Message> success) {
    this(ResultType.GENERIC, query, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the message query to send
   */
  public Result(@NotNull MessageQuery query) {
    this(ResultType.GENERIC, query);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the embed query to send
   * @param success the action to do after the message
   */
  public Result(@NotNull EmbedQuery query, @Nullable Consumer<Message> success) {
    this(ResultType.GENERIC, query, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the embed query to send
   */
  public Result(@NotNull EmbedQuery query) {
    this(ResultType.GENERIC, query);
  }

  /** Create an instance with no message */
  public Result() {
    this(ResultType.GENERIC, null, null, null);
  }

  /**
   * Get the type of the result
   *
   * @return the type of the result
   */
  @NotNull
  public ResultType getType() {
    return this.type;
  }

  /**
   * This success is a callback for when the process is completed
   *
   * @return the callback
   */
  @Nullable
  public Consumer<Message> getSuccess() {
    return this.success;
  }

  /**
   * Get the discord message of the result
   *
   * @return the message of the result
   */
  @Nullable
  public Message getDiscordMessage() {
    return discordMessage;
  }

  /**
   * Get the message of the result
   *
   * @return the message of the result
   */
  @Override
  public @Nullable String getMessage() {
    return message;
  }
}
