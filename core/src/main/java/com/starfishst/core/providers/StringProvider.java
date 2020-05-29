package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link String} */
public class StringProvider<T extends ICommandContext> implements IArgumentProvider<String, T> {

  @Override
  public @NotNull Class<String> getClazz() {
    return String.class;
  }

  @NotNull
  @Override
  public String fromString(@NotNull String string, @NotNull T context) {
    return string;
  }
}
