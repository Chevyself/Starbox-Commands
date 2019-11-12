package com.starfishst.commands.utils;

import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Console {

  @Nullable
  public static Logger logger;

  public Console(@NotNull Logger logger) {
    Console.logger = logger;
  }

  public static void info(@Nullable Object message, Object... strings) {
    if (logger != null) {
      logger.info(Strings.getMessage(message, strings));
    }
  }

  public static void severe(@Nullable Object message, Object... strings) {
    if (logger != null) {
      logger.severe(Strings.getMessage(message, strings));
    }
  }
}
