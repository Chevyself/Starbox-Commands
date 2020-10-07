package com.starfishst.jda;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import me.googas.commons.time.ClassicTime;
import me.googas.commons.time.Time;
import me.googas.commons.time.Unit;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/** The options for different handling in the command manager */
public class ManagerOptions {

  /** Whether or not to delete the message that executes the command */
  private boolean deleteCommands = false;
  /** Whether or not to embed the response messages */
  private boolean embedMessages = true;
  /** Whether or not to delete the response messages that are error */
  private boolean deleteErrors = true;
  /** The time in case the option above is true */
  @NotNull private Time toDeleteErrors = new Time(15, Unit.SECONDS);
  /** Whether or not to delete the response messages t hat are success */
  private boolean deleteSuccess = false;
  /** The time in case the option above is true */
  @NotNull private Time toDeleteSuccess = new Time(15, Unit.SECONDS);
  /** In case that the option above is true */
  @Deprecated private int deleteTime = 15;
  /** In case t hat the option above is true */
  @Deprecated @NotNull private TimeUnit deleteUnit = TimeUnit.SECONDS;
  /** The color to use in success embeds */
  @NotNull private Color success = new Color(0x02e9ff);
  /** The color to use in error embeds */
  @NotNull private Color error = new Color(0xff0202);

  /**
   * Set the option to delete new commands
   *
   * @param deleteCommands the new setting
   */
  public void setDeleteCommands(final boolean deleteCommands) {
    this.deleteCommands = deleteCommands;
  }

  /**
   * Set the time to delete the message
   *
   * @param deleteTime the new time to delete the message
   */
  @Deprecated
  public void setDeleteTime(final int deleteTime) {
    this.deleteTime = deleteTime;
  }

  /**
   * Set if the responses need to be in embeds
   *
   * @param embedMessages the new setting
   */
  public void setEmbedMessages(final boolean embedMessages) {
    this.embedMessages = embedMessages;
  }

  /**
   * Set the setting of deleting errors
   *
   * @param deleteErrors the new setting
   */
  public void setDeleteErrors(final boolean deleteErrors) {
    this.deleteErrors = deleteErrors;
  }

  /**
   * Set the color of errors
   *
   * @param error the new color
   */
  public void setError(@NotNull final Color error) {
    this.error = error;
  }

  /**
   * Set the color of success
   *
   * @param success the color of success
   */
  public void setSuccess(@NotNull final Color success) {
    this.success = success;
  }

  /**
   * Set the unit to delete messages
   *
   * @param deleteUnit the unit to delete messages
   */
  @Deprecated
  public void setDeleteUnit(@NotNull final TimeUnit deleteUnit) {
    this.deleteUnit = deleteUnit;
  }

  /**
   * Set the time to delete errors
   *
   * @param toDeleteErrors the new time to delete errors
   */
  public void setToDeleteErrors(@NotNull Time toDeleteErrors) {
    this.toDeleteErrors = toDeleteErrors;
  }

  /**
   * Set if the bot should delete success messages
   *
   * @param deleteSuccess set to true if you want the bot to delete success messages
   */
  public void setDeleteSuccess(boolean deleteSuccess) {
    this.deleteSuccess = deleteSuccess;
  }

  /**
   * Set the time in which the bot should delete success messages
   *
   * @param toDeleteSuccess the time to delete success messages
   */
  public void setToDeleteSuccess(@NotNull Time toDeleteSuccess) {
    this.toDeleteSuccess = toDeleteSuccess;
  }

  /**
   * Get if the option to delete the message that executes the command is true
   *
   * @return true if set to true
   */
  public boolean isDeleteCommands() {
    return this.deleteCommands;
  }

  /**
   * Get the time value to delete the message
   *
   * @return the delete time
   */
  @Deprecated
  public int getDeleteTime() {
    return this.deleteTime;
  }

  /**
   * Get if the options need to give responses in embeds
   *
   * @return true if the messages are embed
   */
  public boolean isEmbedMessages() {
    return this.embedMessages;
  }

  /**
   * Get if the options need to delete the errors
   *
   * @return true if deletes errors
   */
  public boolean isDeleteErrors() {
    return this.deleteErrors;
  }

  /**
   * Get the color of errors
   *
   * @return the color of errors
   */
  @NotNull
  public Color getError() {
    return this.error;
  }

  /**
   * Get the color of success
   *
   * @return the color of success
   */
  @NotNull
  public Color getSuccess() {
    return this.success;
  }

  /**
   * Get the unit to delete messages
   *
   * @return the unit to delete messages
   */
  @NotNull
  @Deprecated
  public TimeUnit getDeleteUnit() {
    return this.deleteUnit;
  }

  /**
   * The time to delete error messages
   *
   * @return the time to delete error messages
   */
  @NotNull
  public Time getToDeleteErrors() {
    return toDeleteErrors;
  }

  /**
   * Get the consumer to delete errors
   *
   * @return the consumer to delete errors
   */
  public Consumer<Message> getErrorDeleteConsumer() {
    ClassicTime time = getToDeleteErrors().toClassicTime();
    return msg -> msg.delete().queueAfter(time.getValue(), time.getUnit());
  }

  /**
   * Get the consumer to delete success
   *
   * @return the consumer to delete success
   */
  public Consumer<Message> getSuccessDeleteConsumer() {
    ClassicTime time = getToDeleteSuccess().toClassicTime();
    return msg -> msg.delete().queueAfter(time.getValue(), time.getUnit());
  }

  /**
   * Get if the bot should delete success messages
   *
   * @return true if the bot should delete them
   */
  public boolean isDeleteSuccess() {
    return deleteSuccess;
  }

  /**
   * Get the time that the bot should use to delete success messages
   *
   * @return the time to delete success messages
   */
  @NotNull
  public Time getToDeleteSuccess() {
    return toDeleteSuccess;
  }
}
