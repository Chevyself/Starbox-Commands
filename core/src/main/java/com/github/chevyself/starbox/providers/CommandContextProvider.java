package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.providers.type.StarboxExtraArgumentProvider;
import lombok.NonNull;

public abstract class CommandContextProvider<C extends StarboxCommandContext<C, ?>>
    implements StarboxExtraArgumentProvider<C, C> {

  @Override
  public @NonNull C getObject(@NonNull C context) throws ArgumentProviderException {
    return context;
  }
}
