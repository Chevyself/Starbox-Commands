package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.objects.JoinedStrings;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import lombok.NonNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link JoinedStrings} */
public class JoinedStringsProvider<T extends ICommandContext>
    implements IMultipleArgumentProvider<JoinedStrings, T> {

  @NonNull
  @Override
  public JoinedStrings fromStrings(@NonNull String[] strings, @NonNull T context) {
    return new JoinedStrings(strings);
  }

  @Override
  public @NonNull Class<JoinedStrings> getClazz() {
    return JoinedStrings.class;
  }
}
