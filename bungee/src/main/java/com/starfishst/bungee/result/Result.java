package com.starfishst.bungee.result;

import com.starfishst.core.result.SimpleResult;
import org.jetbrains.annotations.NotNull;

/** Result for bungee command execution */
public class Result extends SimpleResult {

  /**
   * Create a new instance of {@link SimpleResult} with a message with place holders
   *
   * @param string the message with place holders
   */
  public Result(@NotNull String string) {
    super(string);
  }
}
