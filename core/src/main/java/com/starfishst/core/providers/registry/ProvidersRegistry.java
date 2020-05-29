package com.starfishst.core.providers.registry;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.providers.type.IContextualProvider;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Contains all the providers ready for the context to use and serialize objects
 *
 * @param <T> the context
 */
public class ProvidersRegistry<T extends ICommandContext> {

  /** The providers that must be given with a context */
  protected List<IContextualProvider<?, T>> providers = new ArrayList<>();

  /**
   * Registers a provider in the providers registry
   *
   * @param provider the provider to register
   */
  public void addProvider(@NotNull IContextualProvider<?, T> provider) {
    providers.add(provider);
  }

  /**
   * Get all the providers for the queried class
   *
   * @param clazz the queried class
   * @return a list of providers for the queried class
   */
  public List<IContextualProvider<?, T>> getProviders(@NotNull Class<?> clazz) {
    return providers.stream()
        .filter(provider -> provider.provides(clazz))
        .collect(Collectors.toList());
  }

  /**
   * Get the object to use as parameter in the invocation of a command.
   *
   * <p>{@link com.starfishst.core.providers.type.ISimpleArgumentProvider#provides(Class)} makes it
   * safe to cast
   *
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the serialized object
   * @throws ArgumentProviderException if the provider is not found or the serialization is not
   *     correct
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public Object getObject(@NotNull Class<?> clazz, @NotNull T context)
      throws ArgumentProviderException {
    for (IContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof IExtraArgumentProvider) {
        return ((IExtraArgumentProvider<?, T>) provider).getObject(context);
      }
    }
    throw new ArgumentProviderException(
        IExtraArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from a string
   *
   * <p>{@link com.starfishst.core.providers.type.ISimpleArgumentProvider#provides(Class)} makes it
   * safe to cast
   *
   * @param string the string to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the serialized object
   * @throws ArgumentProviderException if the provider is not found or the serialization is not
   *     correct
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public Object fromString(@NotNull String string, @NotNull Class<?> clazz, @NotNull T context)
      throws ArgumentProviderException {
    for (IContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof IArgumentProvider) {
        return ((IArgumentProvider<?, T>) provider).fromString(string, context);
      }
    }
    throw new ArgumentProviderException(IArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from strings
   *
   * <p>{@link com.starfishst.core.providers.type.ISimpleArgumentProvider#provides(Class)} makes it
   * safe to cast
   *
   * @param strings the strings to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the serialized object
   * @throws ArgumentProviderException if the provider is not found or the serialization is not
   *     correct
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public Object fromStrings(@NotNull String[] strings, @NotNull Class<?> clazz, @NotNull T context)
      throws ArgumentProviderException {
    for (IContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof IMultipleArgumentProvider) {
        return ((IMultipleArgumentProvider<?, T>) provider).fromStrings(strings, context);
      }
    }
    throw new ArgumentProviderException(
        IMultipleArgumentProvider.class + " was not found for " + clazz);
  }
}
