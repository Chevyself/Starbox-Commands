package me.googas.commands.jda;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.utility.time.Time;
import me.googas.utility.time.unit.Unit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The default implementation for {@link ListenerOptions}
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
public class DefaultListenerOptions implements ListenerOptions {

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

  @NonNull
  private Color getColor(@NonNull ResultType type) {
    if (type.isError()) {
      return this.getError();
    } else {
      return this.getSuccess();
    }
  }

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

  @Override
  public void preCommand(
      @NonNull MessageReceivedEvent event, @NonNull String commandName, @NonNull String[] strings) {
    if (this.isDeleteCommands() && event.getChannelType() != ChannelType.PRIVATE) {
      event.getMessage().delete().queue();
    }
  }

  @Override
  public Message processResult(Result result, @NonNull CommandContext context) {
    if (result != null && result.getDiscordMessage() == null) {
      MessageBuilder builder = new MessageBuilder();
      MessagesProvider messagesProvider = context.getMessagesProvider();
      String thumbnail = messagesProvider.thumbnailUrl(context);
      if (this.isEmbedMessages()) {
        if (result.getMessage() != null) {
          EmbedBuilder embedBuilder =
              new EmbedBuilder()
                  .setTitle(result.getType().getTitle(messagesProvider, context))
                  .setDescription(result.getMessage())
                  .setThumbnail(thumbnail.isEmpty() ? null : thumbnail)
                  .setFooter(messagesProvider.footer(context))
                  .setColor(this.getColor(result.getType()));
          return builder.setEmbed(embedBuilder.build()).build();
        } else {
          return null;
        }
      } else {
        if (result.getMessage() != null) {
          return builder
              .append(
                  messagesProvider.response(
                      result.getType().getTitle(messagesProvider, context),
                      result.getMessage(),
                      context))
              .build();
        } else {
          return null;
        }
      }
    } else if (result != null) {
      return result.getDiscordMessage();
    }
    return null;
  }

  @Override
  public Consumer<Message> processConsumer(Result result, @NonNull CommandContext context) {
    if (result != null) {
      if (result.getSuccess() != null) {
        return result.getSuccess();
      } else if (this.isDeleteErrors() && result.getType().isError()) {
        return this.getErrorDeleteConsumer();
      } else if (this.isDeleteSuccess() && !result.getType().isError()) {
        return this.getSuccessDeleteConsumer();
      }
    }
    return null;
  }
}
