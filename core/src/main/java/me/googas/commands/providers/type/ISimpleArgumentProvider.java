package me.googas.commands.providers.type;

import lombok.NonNull;

/** A simple provider only requires the class of the object */
public interface ISimpleArgumentProvider<O> {

  /**
   * Get if the provider provides with the queried class
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
