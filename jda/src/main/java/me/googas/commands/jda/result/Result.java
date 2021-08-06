package me.googas.commands.jda.result;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.JdaCommand;
import me.googas.commands.result.StarboxResult;
import net.dv8tion.jda.api.entities.Message;

/**
 * This is the implementation for {@link StarboxResult} to be used in the execution of {@link
 * JdaCommand}.
 *
 * <p>The result for {@link JdaCommand} must include
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
public class Result implements StarboxResult {

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
   * null then the content will be added to a {@link Message}.
   */
  private final String message;
  /** Which is the consumer of the {@link #discordMessage} after it is sent. */
  @Getter private final Consumer<Message> success;

  /**
   * Create an instance.
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
   * Create an instance.
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
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   */
  public Result(@NonNull ResultType type, @NonNull Message discordMessage) {
    this(type, discordMessage, null);
  }

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param message the content of the message to send
   * @param success the action to do after the message is sent
   */
  public Result(@NonNull ResultType type, @NonNull String message, Consumer<Message> success) {
    this(type, null, message, success);
  }

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param message the content of the message to send
   */
  public Result(@NonNull ResultType type, @NonNull String message) {
    this(type, message, null);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param discordMessage the discord message to send
   * @param success the action to do after the message is sent
   */
  public Result(@NonNull Message discordMessage, Consumer<Message> success) {
    this(ResultType.GENERIC, discordMessage, success);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param discordMessage the discord message to send
   */
  public Result(@NonNull Message discordMessage) {
    this(ResultType.GENERIC, discordMessage);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param message the content of the message to send
   * @param success the action to do after the message is sent
   */
  public Result(@NonNull String message, Consumer<Message> success) {
    this(ResultType.GENERIC, message, success);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param message the content of the message to send
   */
  public Result(@NonNull String message) {
    this(ResultType.GENERIC, message);
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
    return this.message;
  }
}
