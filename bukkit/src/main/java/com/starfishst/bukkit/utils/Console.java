package com.starfishst.bukkit.utils;

import java.util.logging.Logger;
import org.jetbrains.annotations.Nullable;

/** Easy to use logger for bukkit */
@Deprecated
public class Console {

  @Nullable private static Logger logger = null;

  public static void print(@Nullable String message, Object... strings) {
    if (Console.logger != null) {
      Console.logger.info(BukkitUtils.getMessage(message, strings));
    }
  }

  public static void severe(@Nullable String message, Object... strings) {
    if (Console.logger != null) {
      Console.logger.severe(BukkitUtils.getMessage(message, strings));
    }
  }

  public static void setLogger(@Nullable Logger logger) {
    Console.logger = logger;
  }
}
