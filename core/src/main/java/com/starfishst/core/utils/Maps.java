package com.starfishst.core.utils;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** Utils for maps */
public class Maps {

  /**
   * Create a map starting with single values
   *
   * @param key the key of the map
   * @param value the value of the map
   * @param <T> the type of the key
   * @param <O> the type of the value
   * @return the map with the key and value
   */
  @NotNull
  public static <T, O> HashMap<T, O> singleton(@NotNull T key, @NotNull O value) {
    HashMap<T, O> map = new HashMap<>();
    map.put(key, value);
    return map;
  }
}
