package me.googas.commands.jda;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

/** The options for different handling in the command manager */
@Data
public class ManagerOptions {

  /** Whether or not to delete the message that executes the command */
  private boolean deleteCommands = false;
  /** Whether or not to embed the response messages */
  private boolean embedMessages = true;
  /** Whether or not to delete the response messages that are error */
  private boolean deleteErrors = true;
  /** The time in case the option above is true */
  @NonNull private Duration toDeleteErrors = Duration.of(15, ChronoUnit.SECONDS);
  /** Whether or not to delete the response messages t hat are success */
  private boolean deleteSuccess = false;
  /** The time in case the option above is true */
  @NonNull private Duration toDeleteSuccess = Duration.of(15, ChronoUnit.SECONDS);
  /** The color to use in success embeds */
  @NonNull private Color success = new Color(0x02e9ff);
  /** The color to use in error embeds */
  @NonNull private Color error = new Color(0xff0202);

  /**
   * Get the consumer to delete errors
   *
   * @return the consumer to delete errors
   */
  public Consumer<Message> getErrorDeleteConsumer() {
    return msg -> msg.delete().queueAfter(getToDeleteErrors().toMillis(), TimeUnit.MILLISECONDS);
  }

  /**
   * Get the consumer to delete success
   *
   * @return the consumer to delete success
   */
  public Consumer<Message> getSuccessDeleteConsumer() {
    return msg -> msg.delete().queueAfter(getToDeleteErrors().toMillis(), TimeUnit.MILLISECONDS);
  }
}
