package com.github.chevyself.starbox.providers.registry;

import com.github.chevyself.starbox.ReflectCommand;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ExtraArgument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.parsers.ProviderParser;
import com.github.chevyself.starbox.providers.BooleanProvider;
import com.github.chevyself.starbox.providers.DoubleProvider;
import com.github.chevyself.starbox.providers.DurationProvider;
import com.github.chevyself.starbox.providers.FloatProvider;
import com.github.chevyself.starbox.providers.IntegerProvider;
import com.github.chevyself.starbox.providers.LongProvider;
import com.github.chevyself.starbox.providers.StringProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import com.github.chevyself.starbox.providers.type.StarboxContextualProvider;
import com.github.chevyself.starbox.providers.type.StarboxExtraArgumentProvider;
import com.github.chevyself.starbox.providers.type.StarboxSimpleArgumentProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

/**
 * This registry contains the {@link StarboxArgumentProvider} that gives {@link ReflectCommand} the
 * parameters to be executed with the help of its {@link Argument}. To learn more bout the registry
 * you can check the {@link ReflectCommand#getObjects(StarboxCommandContext)}
 *
 * <p>To add new providers use {@link #addProvider(StarboxContextualProvider)} and to get an object
 * you can use {@link #getObject(Class, StarboxCommandContext)}, {@link #fromString(String, Class,
 * StarboxCommandContext)}.
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
        .addProvider(new DurationProvider<>(messages));
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
   * Registers all providers inside a package.
   *
   * @param packageName the package to register the providers from
   * @return this same instance of registry
   */
  @NonNull
  public ProvidersRegistry<T> addAllIn(@NonNull String packageName) {
    this.addProviders(new ProviderParser<T>(packageName).parseProviders());
    return this;
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
   * {@link ExtraArgument}
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
   * provides a {@link SingleArgument}
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
}
