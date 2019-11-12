package com.starfishst.commands.exceptions;

import org.jetbrains.annotations.Nullable;

public class CommandRegistrationException extends RuntimeException {

  public CommandRegistrationException(@Nullable String message) {
    super(message);
  }
}
