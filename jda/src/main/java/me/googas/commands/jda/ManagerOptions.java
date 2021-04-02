package me.googas.commands.jda;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NonNull;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.time.Time;
import me.googas.commands.time.unit.Unit;
import net.dv8tion.jda.api.entities.Message;

/**
 * The options for different handing in the {@link me.googas.commands.jda.listener.CommandListener}:
 *
 * <ul>
 *   <li>{@link #deleteCommands} whether to delete the message that execute the command
 *   <li>{@link #embedMessages} whether the {@link me.googas.commands.jda.result.Result} of the
 *       command should create an {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *   <li>{@link #deleteErrors} whether the message of the {@link
 *       me.googas.commands.jda.result.Result} when it is a {@link ResultType#isError()} should be
 *       deleted in
 *   <li>{@link #toDeleteErrors} the {@link Time} to delete the message when it is a {@link
 *       ResultType#isError()}
 *   <li>{@link #deleteSuccess} whether the message of the {@link
 *       me.googas.commands.jda.result.Result} when it is not a {@link ResultType#isError()} should
 *       be deleted in
 *   <li>{@link #toDeleteSuccess} the {@link Time} to delete the message when it is not a {@link
 *       ResultType#isError()}
 *   <li>{@link #success} the {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *       when the {@link ResultType} is not {@link ResultType#isError()}
 *   <li>{@link #error} the {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *       when the {@link ResultType} is {@link ResultType#isError()}
 * </ul>
 */
@Data
public class ManagerOptions {

  /** Whether to delete the message that execute the command */
  private boolean deleteCommands = false;
  /**
   * Whether the {@link me.googas.commands.jda.result.Result} of the command should create an {@link
   * net.dv8tion.jda.api.entities.MessageEmbed}
   */
  private boolean embedMessages = true;
  /**
   * Whether the message of the {@link me.googas.commands.jda.result.Result} when it is a {@link
   * ResultType#isError()} should be deleted in
   */
  private boolean deleteErrors = true;
  /** The {@link Time} to delete the message when it is a {@link ResultType#isError()} */
  @NonNull private Time toDeleteErrors = Time.of(15, Unit.SECONDS);
  /**
   * Whether the message of the {@link me.googas.commands.jda.result.Result} when it is not a {@link
   * ResultType#isError()} should be deleted in
   */
  private boolean deleteSuccess = false;
  /** The {@link Time} to delete the message when it is not a {@link ResultType#isError()} */
  @NonNull private Time toDeleteSuccess = Time.of(15, Unit.SECONDS);
  /**
   * The {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed} when the {@link
   * ResultType} is not {@link ResultType#isError()}
   */
  @NonNull private Color success = new Color(0x02e9ff);
  /**
   * The {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed} when the {@link
   * ResultType} is {@link ResultType#isError()}
   */
  @NonNull private Color error = new Color(0xff0202);

  /**
   * Get the consumer to delete errors. This will delete the {@link Message} and {@link
   * net.dv8tion.jda.api.requests.restaction.AuditableRestAction#queueAfter(long, TimeUnit)} the
   * {@link #toDeleteErrors}
   *
   * @see #deleteErrors
   * @see #toDeleteErrors
   * @return the consumer to delete errors
   */
  public Consumer<Message> getErrorDeleteConsumer() {
    return msg ->
        msg.delete().queueAfter(getToDeleteErrors().toMillisRound(), TimeUnit.MILLISECONDS);
  }

  /**
   * Get the consumer to delete success. This will delete the {@link Message} and {@link
   * net.dv8tion.jda.api.requests.restaction.AuditableRestAction#queueAfter(long, TimeUnit)} the
   * {@link #toDeleteSuccess}
   *
   * @see #deleteSuccess
   * @see #toDeleteSuccess
   * @return the consumer to delete success
   */
  public Consumer<Message> getSuccessDeleteConsumer() {
    return msg ->
        msg.delete().queueAfter(getToDeleteSuccess().toMillisRound(), TimeUnit.MILLISECONDS);
  }
}
