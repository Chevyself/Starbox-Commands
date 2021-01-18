package com.starfishst.core.providers.type;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import lombok.NonNull;

/** An extra argument will be in the command method but it is not needed in the command string */
public interface IExtraArgumentProvider<O, T extends ICommandContext>
    extends IContextualProvider<O, T> {

  /**
   * Get the object using the command context
   *
   * @param context the command context
   * @return the {@link com.starfishst.core.arguments.ExtraArgument} object
   * @throws ArgumentProviderException in case that the object could not be given
   */
  @NonNull
  O getObject(@NonNull T context) throws ArgumentProviderException;
}
