package com.starfishst.core.providers.type;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

/** An extra argument will be in the command method but it is not needed in the command string */
public interface IExtraArgumentProvider<O> extends ISimpleArgumentProvider {

  /**
   * Get the object using the command context
   *
   * @param context the command context
   * @return the {@link com.starfishst.core.arguments.ExtraArgument} object
   */
  @NotNull
  O getObject(@NotNull ICommandContext context) throws ArgumentProviderException;
}
