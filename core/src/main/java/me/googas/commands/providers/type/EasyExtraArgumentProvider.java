package me.googas.commands.providers.type;

import lombok.NonNull;
import me.googas.commands.arguments.ExtraArgument;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;

/** An extra argument will be in the command method but it is not needed in the command string */
public interface EasyExtraArgumentProvider<O, T extends EasyCommandContext>
    extends EasyContextualProvider<O, T> {

  /**
   * Get the object using the command context
   *
   * @param context the command context
   * @return the {@link ExtraArgument} object
   * @throws ArgumentProviderException in case that the object could not be given
   */
  @NonNull
  O getObject(@NonNull T context) throws ArgumentProviderException;
}
