package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import lombok.NonNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link String} */
public class StringProvider<T extends ICommandContext> implements IArgumentProvider<String, T> {

  @Override
  public @NonNull Class<String> getClazz() {
    return String.class;
  }

  @NonNull
  @Override
  public String fromString(@NonNull String string, @NonNull T context) {
    return string;
  }
}
