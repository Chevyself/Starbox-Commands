package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderRegistrationException;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.providers.type.StarboxContextualProvider;
import com.github.chevyself.starbox.util.ClassFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 * Parser for {@link StarboxContextualProvider} to initialize and register in {@link
 * ProvidersRegistry}. The registration of the providers using this method is safe as long as all
 * providers have the same context as the one in {@link ProvidersRegistry} as it uses a lot of raw
 * types and unchecked casts. If you want to register providers with different contexts, you should
 * use {@link ProvidersRegistry#addProvider(StarboxContextualProvider)}. This also requires that the
 * providers have a default constructor with no parameters.
 *
 * <p>The use of this registration method may cause {@link ClassCastException} if not properly used.
 *
 * @param <C> the context of the providers
 */
public class ProviderParser<C extends StarboxCommandContext> {

  @NonNull @Getter private final ClassFinder<? extends StarboxContextualProvider<?, C>> providers;

  /**
   * Create a new parser for the providers in the given package.
   *
   * @param packageName the package to search for the providers
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ProviderParser(@NonNull String packageName) {
    this.providers =
        new ClassFinder(StarboxContextualProvider.class, packageName).setRecursive(true);
  }

  /**
   * Parse the providers in the package and return them as a list.
   *
   * @return the list of providers
   */
  @NonNull
  public List<StarboxContextualProvider<?, C>> parseProviders() {
    return this.providers.find().stream()
        .map(
            providerClass -> {
              try {
                Constructor<? extends StarboxContextualProvider<?, C>> constructor =
                    providerClass.getConstructor();
                return (StarboxContextualProvider<?, C>) constructor.newInstance();
              } catch (NoSuchMethodException e) {
                throw new ArgumentProviderRegistrationException(
                    "The provider " + providerClass.getName() + " must have a default constructor");
              } catch (InvocationTargetException
                  | InstantiationException
                  | IllegalAccessException e) {
                throw new ArgumentProviderRegistrationException(
                    "Could not instantiate the provider " + providerClass.getName(), e);
              }
            })
        .collect(Collectors.toList());
  }
}
