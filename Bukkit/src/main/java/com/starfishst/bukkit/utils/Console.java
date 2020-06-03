package com.starfishst.bukkit.utils;

import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/** Easy to use logger for bukkit */
public class Console {

  /** The logger that will be used */
  @Nullable private static Logger logger = null;

  /**
   * Print information with a message with placeholders
   *
   * @param message the message with placeholders
   * @param strings the string to change the placeholders
   */
  public static void print(@Nullable String message, Object... strings) {
    if (Console.logger != null) {
      Console.logger.info(BukkitUtils.getMessage(message, strings));
    }
  }

  /**
   * Print a severe (error) message with placeholders
   *
   * @param message the message with placeholders
   * @param strings the strings to change the placeholders
   */
  public static void severe(@Nullable String message, Object... strings) {
    if (Console.logger != null) {
      Console.logger.severe(BukkitUtils.getMessage(message, strings));
    }
  }

  /**
   * Set the logger that this util with use
   *
   * @param logger the logger to use
   */
  public static void setLogger(@Nullable Logger logger) {
    Console.logger = logger;
  }
}
