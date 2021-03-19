package me.googas.commands.providers.registry;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.BooleanProvider;
import me.googas.commands.providers.DoubleProvider;
import me.googas.commands.providers.IntegerProvider;
import me.googas.commands.providers.JoinedStringsProvider;
import me.googas.commands.providers.LongProvider;
import me.googas.commands.providers.StringProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;
import me.googas.commands.providers.type.EasyContextualProvider;
import me.googas.commands.providers.type.EasyExtraArgumentProvider;
import me.googas.commands.providers.type.EasyMultipleArgumentProvider;
import me.googas.commands.providers.type.EasySimpleArgumentProvider;

/**
 * Contains all the providers ready for the context to use and serialize objects
 *
 * @param <T> the context
 */
public class ProvidersRegistry<T extends EasyCommandContext> {

  /** The providers that must be given with a context */
  protected final List<EasyContextualProvider<?, T>> providers = new ArrayList<>();

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public ProvidersRegistry(@NonNull EasyMessagesProvider<T> messages) {
    this.addProvider(new BooleanProvider<>(messages));
    this.addProvider(new DoubleProvider<>(messages));
    this.addProvider(new IntegerProvider<>(messages));
    this.addProvider(new JoinedStringsProvider<>());
    this.addProvider(new LongProvider<>(messages));
    this.addProvider(new StringProvider<>());
  }

  /** Create the registry with no providers */
  public ProvidersRegistry() {}

  /**
   * Registers a provider in the providers registry
   *
   * @param provider the provider to register
   */
  public void addProvider(@NonNull EasyContextualProvider<?, T> provider) {
    this.providers.add(provider);
  }

  /**
   * Get all the providers for the queried class
   *
   * @param clazz the queried class
   * @return a list of providers for the queried class
   */
  public List<EasyContextualProvider<?, T>> getProviders(@NonNull Class<?> clazz) {
    List<EasyContextualProvider<?, T>> list = new ArrayList<>();
    for (EasyContextualProvider<?, T> provider : this.providers) {
      if (provider.provides(clazz)) {
        list.add(provider);
      }
    }
    return list;
  }

  /**
   * Get the object to use as parameter in the invocation of a command.
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
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
    for (EasyContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof EasyExtraArgumentProvider) {
        return ((EasyExtraArgumentProvider<?, T>) provider).getObject(context);
      }
    }
    throw new ArgumentProviderException(
        EasyExtraArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from a string
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
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
    for (EasyContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof EasyArgumentProvider) {
        return ((EasyArgumentProvider<?, T>) provider).fromString(string, context);
      }
    }
    throw new ArgumentProviderException(EasyArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from strings
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
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
    for (EasyContextualProvider<?, T> provider : getProviders(clazz)) {
      if (provider instanceof EasyMultipleArgumentProvider) {
        return ((EasyMultipleArgumentProvider<?, T>) provider).fromStrings(strings, context);
      }
    }
    throw new ArgumentProviderException(
        EasyMultipleArgumentProvider.class + " was not found for " + clazz);
  }

  /** @see #getObject(Class, EasyCommandContext) */
  @NonNull
  public <O> O get(@NonNull Class<O> clazz, @NonNull T context) throws ArgumentProviderException {
    return clazz.cast(this.getObject(clazz, context));
  }

  /** @see #fromString(String, Class, EasyCommandContext) */
  @NonNull
  public <O> O get(@NonNull String string, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromString(string, clazz, context));
  }

  /** @see #fromStrings(String[], Class, EasyCommandContext) */
  @NonNull
  public <O> O get(@NonNull String[] strings, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromStrings(strings, clazz, context));
  }
}
