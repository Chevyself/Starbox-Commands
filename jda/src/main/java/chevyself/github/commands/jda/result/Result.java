package chevyself.github.commands.jda.result;

import chevyself.github.commands.jda.JdaCommand;
import chevyself.github.commands.jda.listener.CommandListener;
import chevyself.github.commands.result.StarboxResult;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * This is the implementation for {@link StarboxResult} to be used in the execution of {@link
 * JdaCommand}.
 *
 * <p>The result for {@link JdaCommand} must include
 *
 * <ul>
 *   <li>{@link #type} which changes the output in the {@link CommandListener}
 *   <li>{@link #message} the message that will be send to the {@link
 *       net.dv8tion.jda.api.entities.channel.concrete.TextChannel} where the command was executed
 *   <li>{@link #description} the content of the {@link #message} as a {@link String} if {@link
 *       #message} is null then the content will be added to a {@link Message}
 *   <li>{@link #success} which is the consumer of the {@link #message} after it is sent
 * </ul>
 */
public class Result implements JdaResult {

  /** Which changes the output in the {@link CommandListener} depends on the command output. */
  @NonNull @Getter private final ResultType type;
  /**
   * The message that will be sent to the {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel} where the
   * command was executed
   */
  private final MessageCreateData message;
  /**
   * The content of the {@link #message} as a {@link String} if {@link #message} is null then the
   * content will be added to a {@link Message}.
   */
  private final String description;
  /** Which is the consumer of the {@link #message} after it is sent. */
  @Getter private final Consumer<Message> success;

  @Getter private boolean applyCooldown;

  /**
   * Create an instance.
   *
   * @param type the type of the result depending on the command output
   * @param message the discord message to send
   * @param description the content of the message to send
   * @param success the action to do after the message is sent
   */
  protected Result(
      @NonNull ResultType type, MessageCreateData message, String description, Consumer<Message> success) {
    this.type = type;
    this.message = message;
    this.description = description;
    this.success = success;
  }

  /**
   * Start a builder. This will start an empty builder with a type of {@link ResultType#GENERIC}
   *
   * @return the new builder
   */
  @NonNull
  public static Result.Builder builder() {
    return new Builder();
  }

  /**
   * Start a builder using a type.
   *
   * @param type The type to start the builder
   * @return the new builder
   */
  @NonNull
  public static Result.Builder forType(@NonNull ResultType type) {
    return new Builder().setType(type);
  }

  @Override
  public @NonNull Optional<String> getMessage() {
    return Optional.ofNullable(this.description);
  }

  /**
   * Get the discord message of the result.
   *
   * @return a {@link Optional} holding the nullable message
   */
  @NonNull
  public Optional<MessageCreateData> getDiscordMessage() {
    return Optional.ofNullable(this.message);
  }

  @Override
  public @NonNull Result setCooldown(boolean apply) {
    this.applyCooldown = apply;
    return this;
  }

  /** Builder for results. This will help to create a result in a neater way */
  public static class Builder implements JdaResultBuilder {

    @NonNull private ResultType type = ResultType.GENERIC;
    private String description = null;
    private transient Consumer<Message> success = null;
    private transient Supplier<MessageCreateData> messageSupplier = null;
    private boolean cooldown;

    /**
     * Set the type of the result.
     *
     * @param type the new type
     * @return this same instance
     */
    @NonNull
    private Result.Builder setType(@NonNull ResultType type) {
      this.type = type;
      return this;
    }

    /**
     * Set the discord message supplier. If this supplier is not null it will override the message
     * builder
     *
     * @param supplier the message supplier
     * @return this same instance
     */
    @NonNull
    public Result.Builder setMessage(Supplier<MessageCreateData> supplier) {
      this.messageSupplier = supplier;
      return this;
    }

    /**
     * Set the description of the result.
     *
     * @param description the new description
     * @return this same instance
     */
    @NonNull
    public Result.Builder setDescription(String description) {
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
    public Result.Builder next(Consumer<Message> success) {
      this.success = success;
      return this;
    }

    /**
     * Set whether cooldown should apply.
     *
     * @param apply the new value
     * @return this same instance
     */
    public @NonNull Result.Builder setCooldown(boolean apply) {
      this.cooldown = apply;
      return this;
    }

    @Override
    public @NonNull Result build() {
      return new Result(
              type,
              this.messageSupplier == null ? null : this.messageSupplier.get(),
              description,
              success)
          .setCooldown(cooldown);
    }
  }
}
