package com.starfishst.commands.jda.result;

import com.starfishst.commands.jda.utils.embeds.EmbedQuery;
import com.starfishst.commands.jda.utils.message.MessageQuery;
import com.starfishst.core.result.IResult;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commons.builder.ToStringBuilder;
import net.dv8tion.jda.api.entities.Message;

/** This is your general type of result */
public class Result implements IResult {

  /** The type of the result */
  @NonNull @Getter private final ResultType type;
  /** The message send as a result. The discord message will be send over the message string */
  @Getter private final Message discordMessage;
  /** A more simple message to send as a result */
  private final String message;
  /** The action to do with the message after it is sent */
  @Getter private final Consumer<Message> success;

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param discordMessage the discord message to send
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(
      @NonNull ResultType type, Message discordMessage, String message, Consumer<Message> success) {
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
      @NonNull ResultType type, @NonNull Message discordMessage, Consumer<Message> success) {
    this(type, discordMessage, null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param discordMessage the discord message to send
   */
  public Result(@NonNull ResultType type, @NonNull Message discordMessage) {
    this(type, discordMessage, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(@NonNull ResultType type, @NonNull String message, Consumer<Message> success) {
    this(type, null, message, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param message the message to send
   */
  public Result(@NonNull ResultType type, @NonNull String message) {
    this(type, message, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the message query to send
   * @param success the action to do after the message
   */
  public Result(@NonNull ResultType type, @NonNull MessageQuery query, Consumer<Message> success) {
    this(type, query.build(), null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the message query to send
   */
  public Result(@NonNull ResultType type, @NonNull MessageQuery query) {
    this(type, query, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the embed query to send
   * @param success the action to do after the message
   */
  public Result(@NonNull ResultType type, @NonNull EmbedQuery query, Consumer<Message> success) {
    this(type, query.getAsMessageQuery().build(), null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result
   * @param query the embed query to send
   */
  public Result(@NonNull ResultType type, @NonNull EmbedQuery query) {
    this(type, query.getAsMessageQuery().build(), null);
  }

  /**
   * Create an instance with generic result type
   *
   * @param discordMessage the message to send
   * @param success the action to do after the message
   */
  public Result(@NonNull Message discordMessage, Consumer<Message> success) {
    this(ResultType.GENERIC, discordMessage, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param discordMessage the message to send
   */
  public Result(@NonNull Message discordMessage) {
    this(ResultType.GENERIC, discordMessage);
  }

  /**
   * Create an instance with generic result type
   *
   * @param message the message to send
   * @param success the action to do after the message
   */
  public Result(@NonNull String message, Consumer<Message> success) {
    this(ResultType.GENERIC, message, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param message the message to send
   */
  public Result(@NonNull String message) {
    this(ResultType.GENERIC, message);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the message query to send
   * @param success the action to do after the message
   */
  public Result(@NonNull MessageQuery query, Consumer<Message> success) {
    this(ResultType.GENERIC, query, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the message query to send
   */
  public Result(@NonNull MessageQuery query) {
    this(ResultType.GENERIC, query);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the embed query to send
   * @param success the action to do after the message
   */
  public Result(@NonNull EmbedQuery query, Consumer<Message> success) {
    this(ResultType.GENERIC, query, success);
  }

  /**
   * Create an instance with generic result type
   *
   * @param query the embed query to send
   */
  public Result(@NonNull EmbedQuery query) {
    this(ResultType.GENERIC, query);
  }

  /** Create an instance with no message */
  public Result() {
    this(ResultType.GENERIC, null, null, null);
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("type", type)
        .append("discordMessage", discordMessage)
        .append("message", message)
        .append("success", success)
        .build();
  }
}
