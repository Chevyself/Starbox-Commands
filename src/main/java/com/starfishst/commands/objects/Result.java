package com.starfishst.commands.objects;

import org.jetbrains.annotations.Nullable;

public class Result {

  @Nullable private final Object message;
  private final Object[] strings;

  public Result(@Nullable final Object message, final Object... strings) {
    this.message = message;
    this.strings = strings;
  }

  public Result() {
    this(null);
  }

  @Nullable
  public Object getMessage() {
    return this.message;
  }

  public Object[] getStrings() {
    return strings;
  }
}
