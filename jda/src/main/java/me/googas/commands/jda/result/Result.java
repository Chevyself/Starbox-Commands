package me.googas.commands.jda.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.JdaCommand;
import me.googas.commands.result.StarboxResult;
import me.googas.starbox.builders.Builder;
import net.dv8tion.jda.api.MessageBuilder;
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
 *   <li>{@link #description} the content of the {@link #discordMessage} as a {@link String} if
 *       {@link #discordMessage} is null then the content will be added to a {@link Message}
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
  private final String description;
  /** Which is the consumer of the {@link #discordMessage} after it is sent. */
  @Getter private final Consumer<Message> success;

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   * @param description the content of the message to send
   * @param success the action to do after the message is sent
   */
  protected Result(
      @NonNull ResultType type,
      Message discordMessage,
      String description,
      Consumer<Message> success) {
    this.type = type;
    this.discordMessage = discordMessage;
    this.description = description;
    this.success = success;
  }

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param discordMessage the discord message to send
   * @param success the action to do after the message is sent
   */
  @Deprecated
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
  @Deprecated
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
  @Deprecated
  public Result(@NonNull ResultType type, @NonNull String message, Consumer<Message> success) {
    this(type, null, message, success);
  }

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param message the content of the message to send
   */
  @Deprecated
  public Result(@NonNull ResultType type, @NonNull String message) {
    this(type, message, null);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param discordMessage the discord message to send
   * @param success the action to do after the message is sent
   */
  @Deprecated
  public Result(@NonNull Message discordMessage, Consumer<Message> success) {
    this(ResultType.GENERIC, discordMessage, success);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param discordMessage the discord message to send
   */
  @Deprecated
  public Result(@NonNull Message discordMessage) {
    this(ResultType.GENERIC, discordMessage);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param message the content of the message to send
   * @param success the action to do after the message is sent
   */
  @Deprecated
  public Result(@NonNull String message, Consumer<Message> success) {
    this(ResultType.GENERIC, message, success);
  }

  /**
   * Create an instance this will use the {@link ResultType} as {@link ResultType#GENERIC}.
   *
   * @param message the content of the message to send
   */
  @Deprecated
  public Result(@NonNull String message) {
    this(ResultType.GENERIC, message);
  }

  /**
   * Create an empty instance which will not send anything to the {@link
   * net.dv8tion.jda.api.entities.TextChannel} where the command was executed
   */
  @Deprecated
  public Result() {
    this(ResultType.GENERIC, null, null, null);
  }

  /**
   * Start a builder. This will start an empty builder with a type of {@link ResultType#GENERIC}
   *
   * @return the new builder
   */
  @NonNull
  public static ResultBuilder builder() {
    return new ResultBuilder();
  }

  /**
   * Start a builder using a type.
   *
   * @param type The type to start the builder
   * @return the new builder
   */
  @NonNull
  public static ResultBuilder forType(@NonNull ResultType type) {
    return new ResultBuilder().setType(type);
  }

  @Override
  public @NonNull Optional<String> getMessage() {
    return Optional.ofNullable(this.description);
  }

  /** Builder for results. This will help to create a result in a neater way */
  public static class ResultBuilder implements Builder<Result> {

    @NonNull private ResultType type = ResultType.GENERIC;
    @Getter private MessageBuilder message = new MessageBuilder();
    private String description = null;
    private transient Consumer<Message> success = null;
    private transient Supplier<Message> messageSupplier = null;

    /**
     * Set the type of the result.
     *
     * @param type the new type
     * @return this same instance
     */
    @NonNull
    private ResultBuilder setType(@NonNull ResultType type) {
      this.type = type;
      return this;
    }

    /**
     * Set the message builder of the result.
     *
     * @param message the new builder
     * @return this same instance
     */
    @NonNull
    public ResultBuilder setMessage(@NonNull MessageBuilder message) {
      this.message = message;
      return this;
    }

    /**
     * Set the discord message supplier. If this supplier is not null it will override the message
     * builder
     *
     * @param messageSupplier the message supplier
     * @return this same instance
     */
    @NonNull
    public ResultBuilder setMessage(Supplier<Message> messageSupplier) {
      this.messageSupplier = messageSupplier;
      return this;
    }

    /**
     * Set the description of the result.
     *
     * @param description the new description
     * @return this same instance
     */
    @NonNull
    public ResultBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    /**
     * Set the consumer for the message.
     *
     * @param success the consumer for the message
     * @return this same instance
     */
    @NonNull
    public ResultBuilder next(Consumer<Message> success) {
      this.success = success;
      return this;
    }

    @Override
    public @NonNull Result build() {
      return new Result(
          type,
          this.messageSupplier == null ? this.message.build() : this.messageSupplier.get(),
          description,
          success);
    }
  }
}
