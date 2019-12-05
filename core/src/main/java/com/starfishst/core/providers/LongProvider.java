package com.starfishst.core.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class LongProvider implements IArgumentProvider<Long> {

  @Override
  public @NotNull Class<Long> getClazz() {
    return long.class;
  }

  @NotNull
  @Override
  public Long fromString(@NotNull String string) throws ArgumentProviderException {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("{0} is not a valid long format", string);
    }
  }
}
