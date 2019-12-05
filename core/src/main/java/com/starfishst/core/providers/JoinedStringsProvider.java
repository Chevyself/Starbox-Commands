package com.starfishst.core.providers;

import com.starfishst.core.arguments.JoinedStrings;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class JoinedStringsProvider implements IMultipleArgumentProvider<JoinedStrings> {

  @NotNull
  @Override
  public JoinedStrings fromStrings(@NotNull String[] strings) {
    return new JoinedStrings(strings);
  }

  @Override
  public @NotNull Class<JoinedStrings> getClazz() {
    return JoinedStrings.class;
  }
}
