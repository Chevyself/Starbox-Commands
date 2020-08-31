package com.starfishst.core.utils.maps;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/** Helps with single line hash map building */
public class MapBuilder<K, V> {

  /** The hash map that is being built */
  @NotNull private final HashMap<K, V> hashMap;

  /**
   * Create a map builder
   *
   * @param hashMap the map that is being built
   */
  @NotNull
  public MapBuilder(@NotNull HashMap<K, V> hashMap) {
    this.hashMap = hashMap;
  }

  /**
   * Appends values to the map
   *
   * @param key with which the value is associated
   * @param value the value associated with the key
   * @return the map builder
   */
  public MapBuilder<K, V> append(@NotNull K key, @NotNull V value) {
    hashMap.put(key, value);
    return this;
  }

  /**
   * Appends another map to the builder
   *
   * @param map mappings to be stored in this map
   * @return the map builder
   */
  public MapBuilder<K, V> appendAll(@NotNull Map<? extends K, ? extends V> map) {
    hashMap.putAll(map);
    return this;
  }

  /**
   * Get the built map
   *
   * @return the built map
   */
  @NotNull
  public HashMap<K, V> build() {
    return hashMap;
  }
}
