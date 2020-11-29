package com.starfishst.core.providers.registry;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.BooleanProvider;
import com.starfishst.core.providers.DoubleProvider;
import com.starfishst.core.providers.IntegerProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.TimeProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.providers.type.IContextualProvider;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

/**
 * Contains all the providers ready for the context to use and serialize objects
 *
 * @param <T> the context
 */
public class ProvidersRegistry<T extends ICommandContext> {

  /** The providers that must be given with a context */
  protected final List<IContextualProvider<?, T>> providers = new ArrayList<>();

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public ProvidersRegistry(@NonNull IMessagesProvider<T> messages) {
    this.addProvider(new BooleanProvider<>(messages));
    this.addProvider(new DoubleProvider<>(messages));
    this.addProvider(new IntegerProvider<>(messages));
    this.addProvider(new JoinedStringsProvider<>());
    this.addProvider(new LongProvider<>(messages));
    this.addProvider(new StringProvider<>());
    this.addProvider(new TimeProvider<>(messages));
  }

  /** Create the registry with the default providers */
  public ProvidersRegistry() {}

  /**
   * Registers a provider in the providers registry
   *
   * @param provider the provider to register
   */
  public void addProvider(@NonNull IContextualProvider<?, T> provider) {
    this.providers.add(provider);
  }

  /**
   * Get all the providers for the queried class
   *
   * @param clazz the queried class
   * @return a list of providers for the queried class
   */
  public List<IContextualProvider<?, T>> getProviders(@NonNull Class<?> clazz) {
    List<IContextualProvider<?, T>> list = new ArrayList<>();
    for (IContextualProvider<?, T> provider : this.providers) {
      if (provider.provides(clazz)) {
        list.add(provider);
      }
    }
    return list;
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
  @NonNull
  public Object getObject(@NonNull Class<?> clazz, @NonNull T context)
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
  @NonNull
  public Object fromString(@NonNull String string, @NonNull Class<?> clazz, @NonNull T context)
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
  @NonNull
  public Object fromStrings(@NonNull String[] strings, @NonNull Class<?> clazz, @NonNull T context)
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
