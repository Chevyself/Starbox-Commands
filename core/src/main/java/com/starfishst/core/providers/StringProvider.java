package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class StringProvider implements IArgumentProvider<String> {

  @Override
  public @NotNull Class<String> getClazz() {
    return String.class;
  }

  @NotNull
  @Override
  public String fromString(@NotNull String string, @NotNull ICommandContext<?> context) {
    return string;
  }
}
