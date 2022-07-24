package me.googas.commands.providers.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.BooleanProvider;
import me.googas.commands.providers.DoubleProvider;
import me.googas.commands.providers.FloatProvider;
import me.googas.commands.providers.IntegerProvider;
import me.googas.commands.providers.LongProvider;
import me.googas.commands.providers.StringProvider;
import me.googas.commands.providers.TimeProvider;
import me.googas.commands.providers.type.StarboxArgumentProvider;
import me.googas.commands.providers.type.StarboxContextualProvider;
import me.googas.commands.providers.type.StarboxExtraArgumentProvider;
import me.googas.commands.providers.type.StarboxMultipleArgumentProvider;
import me.googas.commands.providers.type.StarboxSimpleArgumentProvider;

/**
 * This registry contains the {@link StarboxArgumentProvider} that gives {@link
 * me.googas.commands.ReflectCommand} the parameters to be executed with the help of its {@link
 * me.googas.commands.arguments.Argument} to learn more bout the registry you can check the {@link
 * me.googas.commands.ReflectCommand#getObjects(StarboxCommandContext)}
 *
 * <p>To add new providers use {@link #addProvider(StarboxContextualProvider)} and to get an object
 * you can use {@link #getObject(Class, StarboxCommandContext)}, {@link #fromString(String, Class,
 * StarboxCommandContext)} or {@link #fromStrings(String[], Class, StarboxCommandContext)}
 */
public class ProvidersRegistry<T extends StarboxCommandContext> {

  /** The providers that must be given with a context. */
  protected final List<StarboxContextualProvider<?, T>> providers = new ArrayList<>();

  /**
   * Create the registry with the default providers.
   *
   * @param messages the messages' provider for the messages sent in the default providers
   */
  public ProvidersRegistry(@NonNull StarboxMessagesProvider<T> messages) {
    this.addProvider(new BooleanProvider<>(messages))
        .addProvider(new DoubleProvider<>(messages))
        .addProvider(new FloatProvider<>(messages))
        .addProvider(new IntegerProvider<>(messages))
        .addProvider(new LongProvider<>(messages))
        .addProvider(new StringProvider<>())
        .addProvider(new TimeProvider<>(messages));
  }

  /** Creates the registry with no providers. */
  public ProvidersRegistry() {}

  /**
   * Registers a provider in the providers' registry.
   *
   * @param provider the provider to register
   * @return this same instance of registry
   */
  @NonNull
  public ProvidersRegistry<T> addProvider(@NonNull StarboxContextualProvider<?, T> provider) {
    this.providers.add(provider);
    return this;
  }

  /**
   * Registers many providers in the providers' registry.
   *
   * @param providers the providers to register
   * @return this same instance of registry
   */
  @NonNull
  public ProvidersRegistry<T> addProviders(
      @NonNull Collection<StarboxContextualProvider<?, T>> providers) {
    for (StarboxContextualProvider<?, T> provider : providers) {
      this.addProvider(provider);
    }
    return this;
  }

  /**
   * Registers many providers in the providers' registry.
   *
   * @param providers the providers to register
   * @return this same instance of registry
   */
  @SafeVarargs
  @NonNull
  public final ProvidersRegistry<T> addProviders(
      @NonNull StarboxContextualProvider<?, T>... providers) {
    return this.addProviders(Arrays.asList(providers));
  }

  /**
   * Get all the providers that provide the queried class.
   *
   * <p>For example if you have the provider
   *
   * <pre>{@code
   * public class StringProvider&lt;T extends StarboxCommandContext&gt; implements StarboxArgumentProvider&lt;String, T&gt; {
   *
   *  &#64;Override
   *   public @NonNull Class&lt;String&gt; getClazz() {
   *     return String.class;
   *   }
   *
   *   &#64;NonNull
   *   &#64;Override
   *   public String fromString(@NonNull String string, @NonNull T context) {
   *     return string;
   *   }
   * }
   * }</pre>
   *
   * <p>And you parseAndRegister it using {@link #addProvider(StarboxContextualProvider)}
   *
   * <p>You can get it with this method with {@link String#getClass()} ()}
   *
   * @param clazz the queried class
   * @return a list of providers for the queried class
   */
  @NonNull
  public List<StarboxContextualProvider<?, T>> getProviders(@NonNull Class<?> clazz) {
    List<StarboxContextualProvider<?, T>> list = new ArrayList<>();
    for (StarboxContextualProvider<?, T> provider : this.providers) {
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
   * StarboxExtraArgumentProvider}
   *
   * <p>{@link StarboxSimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link StarboxExtraArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link
   *     StarboxExtraArgumentProvider#getObject(StarboxCommandContext)}
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public Object getObject(@NonNull Class<?> clazz, @NonNull T context)
      throws ArgumentProviderException {
    for (StarboxContextualProvider<?, T> provider : this.getProviders(clazz)) {
      if (provider instanceof StarboxExtraArgumentProvider) {
        return ((StarboxExtraArgumentProvider<?, T>) provider).getObject(context);
      }
    }
    throw new ArgumentProviderException(
        StarboxExtraArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from a string. This object
   * provides a {@link me.googas.commands.arguments.SingleArgument}
   *
   * <p>We will get all the providers using {@link #getProviders(Class)} to get a {@link
   * StarboxArgumentProvider}
   *
   * <p>{@link StarboxSimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param string the string to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link StarboxExtraArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link StarboxArgumentProvider#fromString(String,
   *     StarboxCommandContext)}
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public Object fromString(@NonNull String string, @NonNull Class<?> clazz, @NonNull T context)
      throws ArgumentProviderException {
    for (StarboxContextualProvider<?, T> provider : this.getProviders(clazz)) {
      if (provider instanceof StarboxArgumentProvider) {
        return ((StarboxArgumentProvider<?, T>) provider).fromString(string, context);
      }
    }
    throw new ArgumentProviderException(
        StarboxArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * Get the object to use as a parameter in the invocation of a command from strings. This object
   * provides a {@link me.googas.commands.arguments.MultipleArgument}
   *
   * <p>We will get all the providers using {@link #getProviders(Class)} to get a {@link
   * StarboxMultipleArgumentProvider}
   *
   * <p>{@link StarboxSimpleArgumentProvider#provides(Class)} makes it safe to cast
   *
   * @param strings the strings to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @return the object provided by the {@link StarboxMultipleArgumentProvider}
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link
   *     StarboxMultipleArgumentProvider#fromStrings(String[], StarboxCommandContext)}
   */
  @SuppressWarnings("unchecked")
  @NonNull
  @Deprecated
  public Object fromStrings(@NonNull String[] strings, @NonNull Class<?> clazz, @NonNull T context)
      throws ArgumentProviderException {
    for (StarboxContextualProvider<?, T> provider : this.getProviders(clazz)) {
      if (provider instanceof StarboxMultipleArgumentProvider) {
        return ((StarboxMultipleArgumentProvider<?, T>) provider).fromStrings(strings, context);
      }
    }
    throw new ArgumentProviderException(
        StarboxMultipleArgumentProvider.class + " was not found for " + clazz);
  }

  /**
   * This method uses {@link #getObject(Class, StarboxCommandContext)} and casts the returned object
   * as it is safe to do so.
   *
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provided
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link
   *     StarboxExtraArgumentProvider#getObject(StarboxCommandContext)}
   */
  @NonNull
  public <O> O get(@NonNull Class<O> clazz, @NonNull T context) throws ArgumentProviderException {
    return clazz.cast(this.getObject(clazz, context));
  }

  /**
   * This method uses {@link #fromString(String, Class, StarboxCommandContext)} and casts the
   * returned object as it is safe to do so.
   *
   * @param string the string to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provider
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link StarboxArgumentProvider}
   */
  @NonNull
  public <O> O get(@NonNull String string, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromString(string, clazz, context));
  }

  /**
   * This method uses {@link #fromStrings(String[], Class, StarboxCommandContext)} and casts the
   * returned object as it is safe to do so.
   *
   * @param strings the strings to get the object from
   * @param clazz the clazz to get the provider from
   * @param context the context of the command execution
   * @param <O> the type of object to get from the provider
   * @return the object returned by the provider
   * @throws ArgumentProviderException if the provider is not found or the provider cannot provide
   *     the object for some reason, see {@link StarboxMultipleArgumentProvider}
   */
  @NonNull
  @Deprecated
  public <O> O get(@NonNull String[] strings, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    return clazz.cast(this.fromStrings(strings, clazz, context));
  }
}
