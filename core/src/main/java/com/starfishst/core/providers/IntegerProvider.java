package com.starfishst.core.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class IntegerProvider implements IArgumentProvider<Integer> {

  @Override
  public @NotNull Class<Integer> getClazz() {
    return int.class;
  }

  @NotNull
  @Override
  public Integer fromString(@NotNull String string) throws ArgumentProviderException {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("{0} is not the correct format for integer", string);
    }
  }
}
