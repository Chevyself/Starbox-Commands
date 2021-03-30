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
import me.googas.commands.providers.TimeProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;
import me.googas.commands.providers.type.EasyContextualProvider;
import me.googas.commands.providers.type.EasyExtraArgumentProvider;
import me.googas.commands.providers.type.EasyMultipleArgumentProvider;
import me.googas.commands.providers.type.EasySimpleArgumentProvider;

/**
 * This registry contains the {@link EasyArgumentProvider} that gives {@link
 * me.googas.commands.ReflectCommand} the parameters to be executed with the help of its {@link
 * me.googas.commands.arguments.Argument} to learn more bout the registry you can check the {@link
 * me.googas.commands.ReflectCommand#getObjects(EasyCommandContext)}
 *
 * <p>To add new providers use {@link #addProvider(EasyContextualProvider)} and to get an object you
 * can use {@link #getObject(Class, EasyCommandContext)}, {@link #fromString(String, Class,
 * EasyCommandContext)} or {@link #fromStrings(String[], Class, EasyCommandContext)}
 */
public class ProvidersRegistry<T extends EasyCommandContext> {

  /** The providers that must be given with a context */
  protected final List<EasyContextualProvider<?, T>> providers = new ArrayList<>();

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages provider for the messages sent in the default providers
   */
  public ProvidersRegistry(@NonNull EasyMessagesProvider<T> messages) {
    this.addProvider(new BooleanProvider<>(messages));
    this.addProvider(new DoubleProvider<>(messages));
    this.addProvider(new IntegerProvider<>(messages));
    this.addProvider(new JoinedStringsProvider<>());
    this.addProvider(new LongProvider<>(messages));
    this.addProvider(new StringProvider<>());
    this.addProvider(new TimeProvider<>(messages));
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
   * Get all the providers that provide the queried class.
   *
   * <p>For example if you have the provider
   *
   * <pre>
   * public class StringProvider<T extends EasyCommandContext>
   *     implements EasyArgumentProvider<String, T> {
   *
   *   @Override
   *   public @NonNull Class<String> getClazz() {
   *     return String.class;
   *   }
   *
   *   @NonNull
   *   @Override
   *   public String fromString(@NonNull String string, @NonNull T context) {
   *     return string;
   *   }
   * }
   * </pre>
   *
   * And you register it using {@link #addProvider(EasyContextualProvider)}
   *
   * <p>You can get it with this method with {@link String#getClass()}
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
   * Get the object to use as parameter in the invocation of a command. This object provides an
   * {@link me.googas.commands.arguments.ExtraArgument}
   *
   * <p>We will get all the providers using {@link #getProviders(Class)} to get a {@link
   * EasyExtraArgumentProvider}
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link EasyExtraArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link
   *     EasyExtraArgumentProvider#getObject(EasyCommandContext)}
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
   * Get the object to use as a parameter in the invocation of a command from a string. This object
   * provides a {@link me.googas.commands.arguments.SingleArgument}
   *
   * <p>We will get all the providers using {@link #getProviders(Class)} to get a {@link
   * EasyArgumentProvider}
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param string the string to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link EasyExtraArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link EasyArgumentProvider#fromString(String,
   *     EasyCommandContext)}
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
   * Get the object to use as a parameter in the invocation of a command from strings. This object
   * provides a {@link me.googas.commands.arguments.MultipleArgument}
   *
   * <p>We will get all the providers using {@link #getProviders(Class)} to get a {@link
   * EasyMultipleArgumentProvider}
   *
   * <p>{@link EasySimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param strings the strings to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link EasyMultipleArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link EasyMultipleArgumentProvider#fromStrings(String[],
   *     EasyCommandContext)}
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

  /**
   * This method uses {@link #getObject(Class, EasyCommandContext)} and casts the returned object as
   * it is safe to do so
   *
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provided
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link
   *     EasyExtraArgumentProvider#getObject(EasyCommandContext)}
   */
  @NonNull
  public <O> O get(@NonNull Class<O> clazz, @NonNull T context) throws ArgumentProviderException {
    return clazz.cast(this.getObject(clazz, context));
  }

  /**
   * This method uses {@link #fromString(String, Class, EasyCommandContext)} and casts the returned
   * object as it is safe to do so
   *
   * @param string the string to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provider
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link EasyArgumentProvider}
   */
  @NonNull
  public <O> O get(@NonNull String string, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromString(string, clazz, context));
  }

  /**
   * This method uses {@link #fromStrings(String[], Class, EasyCommandContext)} and casts the
   * returned object as it is safe to do so
   *
   * @param strings the strings to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provider
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link EasyMultipleArgumentProvider}
   */
  @NonNull
  public <O> O get(@NonNull String[] strings, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromStrings(strings, clazz, context));
  }
}
