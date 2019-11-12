package com.starfishst.commands.exceptions;

import com.starfishst.commands.utils.Strings;
import org.jetbrains.annotations.NotNull;

public class ArgumentProviderException extends Throwable {

  public ArgumentProviderException(@NotNull String message, Object... strings) {
    super(Strings.getMessage(message, strings));
  }
}
