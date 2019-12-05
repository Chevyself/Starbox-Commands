package com.starfishst.core.result;

import org.jetbrains.annotations.Nullable;

/** The result is a message send by the command to the sender from it's execution */
public interface IResult {

  /**
   * Get the result message
   *
   * @return the result message
   */
  @Nullable
  String getMessage();
}
