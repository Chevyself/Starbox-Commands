package me.googas.commands.providers.type;

import lombok.NonNull;

/**
 * This is the interface that every provider has to extend or implement
 *
 * <p>The first implementation to check is {@link EasyContextualProvider}
 *
 * @param <O> the type of object to provide
 */
public interface EasySimpleArgumentProvider<O> {

  /**
   * Get if the provider provides with the queried class
   *
   * <p>By default this will check using {@link #getClazz()} {@link Class#isAssignableFrom(Class)}
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  default boolean provides(@NonNull Class<?> clazz) {
    return clazz.isAssignableFrom(getClazz());
  }

  /**
   * Get the class to provide
   *
   * @return the class to provide
   */
  @NonNull
  Class<O> getClazz();
}
