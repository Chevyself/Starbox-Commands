package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

public abstract class MultipleArgumentProvider<O> extends ArgumentProvider<O> {

  MultipleArgumentProvider(@NotNull Class<O> clazz) {
    super(clazz);
  }

  public abstract O fromStrings(@NotNull String[] strings) throws ArgumentProviderException;
}
