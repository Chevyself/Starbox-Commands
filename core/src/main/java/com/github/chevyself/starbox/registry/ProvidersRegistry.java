package com.github.chevyself.starbox.registry;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ExtraArgument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ProvidersRegistry<T extends StarboxCommandContext<T, ?>> {

  @NonNull private final List<StarboxArgumentProvider<?, T>> providers = new ArrayList<>();

  @NonNull
  private final List<StarboxExtraArgumentProvider<?, T>> extraProviders = new ArrayList<>();

  @NonNull
  private final Map<Class<?>, StarboxArgumentProvider<?, T>> providersCache = new HashMap<>();

  @NonNull
  private final Map<Class<?>, StarboxExtraArgumentProvider<?, T>> extraProvidersCache =
      new HashMap<>();

  /**
   * Create the registry with the default providers.
   *
   * @param messages the messages' provider for the messages sent in the default providers
   */
  public ProvidersRegistry(@NonNull MessagesProvider<T> messages) {
    this.registerDefaults(messages);
  }

  public void registerDefaults(@NonNull MessagesProvider<T> messages) {
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
    if (provider instanceof StarboxArgumentProvider<?, ?>) {
      this.providers.add((StarboxArgumentProvider<?, T>) provider);
    } else if (provider instanceof StarboxExtraArgumentProvider<?, ?>) {
      this.extraProviders.add((StarboxExtraArgumentProvider<?, T>) provider);
    }
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

  public StarboxArgumentProvider<?, T> getProvider(@NonNull Class<?> clazz) {
    StarboxArgumentProvider<?, T> cached = this.providersCache.get(clazz);
    if (cached == null) {
      cached =
          this.providers.stream()
              .filter(provider -> provider.provides(clazz))
              .findFirst()
              .orElse(null);
      this.providersCache.put(clazz, cached);
    }
    return cached;
  }

  public StarboxExtraArgumentProvider<?, T> getExtraProvider(@NonNull Class<?> clazz) {
    StarboxExtraArgumentProvider<?, T> cached = this.extraProvidersCache.get(clazz);
    if (cached == null) {
      cached =
          this.extraProviders.stream()
              .filter(provider -> provider.provides(clazz))
              .findFirst()
              .orElse(null);
      this.extraProvidersCache.put(clazz, cached);
    }
    return cached;
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
   * @param <O> the type of object to get from the provider
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public <O> O getObject(@NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    StarboxExtraArgumentProvider<?, T> provider = this.getExtraProvider(clazz);
    if (provider != null) {
      return (O) provider.getObject(context);
    } else {
      throw new ArgumentProviderException(
          String.format(
              "Extra argument provider has not been registered for '%s', please ensure registration or the proper use of #provides(Class)",
              clazz.getName()));
    }
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
   * @param <O> the type of object to get from the provider
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public <O> O fromString(@NonNull String string, @NonNull Class<O> clazz, @NonNull T context)
      throws ArgumentProviderException {
    StarboxArgumentProvider<?, T> provider = this.getProvider(clazz);
    if (provider != null) {
      return (O) provider.fromString(string, context);
    } else {
      throw new ArgumentProviderException(
          String.format(
              "Argument provider has not been registered for '%s', please ensure registration or the proper use of #provides(Class)",
              clazz.getName()));
    }
  }
}
