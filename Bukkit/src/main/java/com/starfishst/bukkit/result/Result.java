package com.starfishst.bukkit.result;

import com.starfishst.core.result.SimpleResult;
import com.starfishst.core.utils.Strings;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** The result that can be send by the execution of the command */
public class Result extends SimpleResult {

  /**
   * A result with a message
   *
   * @param string the message to send as result
   */
  public Result(@NotNull String string) {
    super(string);
  }

  /**
   * A result with a message that contains placeholders
   *
   * @param string the message to send as result
   * @param placeholders the placeholders to change in the message
   */
  public Result(@NotNull String string, @NotNull HashMap<String, String> placeholders) {
    this(Strings.buildMessage(string, placeholders));
  }

  /** A result without message */
  public Result() {
    super();
  }
}
