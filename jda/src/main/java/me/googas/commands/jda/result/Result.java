package me.googas.commands.jda.result;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.utils.embeds.EmbedQuery;
import me.googas.commands.jda.utils.message.MessageQuery;
import me.googas.commands.result.EasyResult;
import net.dv8tion.jda.api.entities.Message;

/**
 * This is the implementation for {@link EasyResult} to be used in the execution of {@link
 * me.googas.commands.jda.EasyJdaCommand}.
 *
 * <p>The result for {@link me.googas.commands.jda.EasyJdaCommand} must include
 *
 * <ul>
 *   <li>{@link #type} which changes the output in the {@link
 *       me.googas.commands.jda.listener.CommandListener}
 *   <li>{@link #discordMessage} the message that will be send to the {@link
 *       net.dv8tion.jda.api.entities.TextChannel} where the command was executed
 *   <li>{@link #message} the content of the {@link #discordMessage} as a {@link String} if {@link
 *       #discordMessage} is null then the content will be added to a {@link Message}
 *   <li>{@link #success} which is the consumer of the {@link #discordMessage} after it is sent
 * </ul>
 */
public class Result implements EasyResult {

  /**
   * Which changes the output in the {@link me.googas.commands.jda.listener.CommandListener} depends
   * on the command output
   */
  @NonNull @Getter private final ResultType type;
  /**
   * The message that will be send to the {@link net.dv8tion.jda.api.entities.TextChannel} where the
   * command was executed
   */
  @Getter private final Message discordMessage;
  /**
   * The content of the {@link #discordMessage} as a {@link String} if {@link #discordMessage} is
   * null then the content will be added to a {@link Message}
   */
  private final String message;
  /** Which is the consumer of the {@link #discordMessage} after it is sent */
  @Getter private final Consumer<Message> success;

  /**
   * Create an instance
   *
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   * @param message the content of the message to send
   * @param success the action to do after the message is sent
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
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   * @param success the action to do after the message is sent
   */
  public Result(
      @NonNull ResultType type, @NonNull Message discordMessage, Consumer<Message> success) {
    this(type, discordMessage, null, success);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   */
  public Result(@NonNull ResultType type, @NonNull Message discordMessage) {
    this(type, discordMessage, null);
  }

  /**
   * Create an instance
   *
   * @param type the type of the result depending on the command output
   * @param message the content of the message to send
   * @param success the action to do after the message is sent
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

  /**
   * Create an empty instance which will not send anything to the {@link
   * net.dv8tion.jda.api.entities.TextChannel} where the command was executed
   */
  public Result() {
    this(ResultType.GENERIC, null, null, null);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
