package com.starfishst.core.result;

import com.starfishst.core.utils.Strings;
import org.jetbrains.annotations.Nullable;

public class SimpleResult implements IResult {

  @Nullable private final String message;

  /**
   * Create a new instance of {@link SimpleResult} with a message with place holders
   *
   * @param string the message with place holders
   * @param strings the place holders
   */
  protected SimpleResult(@Nullable String string, Object... strings) {
    this.message = Strings.buildMessage(string, strings);
  }

  /** Create a new instance of {@link SimpleResult} with no message; */
  protected SimpleResult() {
    this.message = null;
  }

  @Nullable
  @Override
  public String getMessage() {
    return this.message;
  }
}
