package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.objects.JoinedStrings;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link JoinedStrings} */
public class JoinedStringsProvider<T extends ICommandContext>
    implements IMultipleArgumentProvider<JoinedStrings, T> {

  @NotNull
  @Override
  public JoinedStrings fromStrings(@NotNull String[] strings, @NotNull T context) {
    return new JoinedStrings(strings);
  }

  @Override
  public @NotNull Class<JoinedStrings> getClazz() {
    return JoinedStrings.class;
  }
}
