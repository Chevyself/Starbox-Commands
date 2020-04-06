package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.custom.CustomNumber;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class NumberProvider implements IArgumentProvider<CustomNumber> {

  @NotNull
  @Override
  public CustomNumber fromString(@NotNull String string, @NotNull ICommandContext<?> context)
          throws ArgumentProviderException {
    try {
      return new CustomNumber(Long.parseLong(string));
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("{0} is not a valid long format", string);
    }
  }

  @Override
  public @NotNull Class<CustomNumber> getClazz() {
    return CustomNumber.class;
  }
}
