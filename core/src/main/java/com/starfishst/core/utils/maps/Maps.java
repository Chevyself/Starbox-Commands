package com.starfishst.core.utils.maps;

import com.starfishst.core.utils.Strings;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public static <T, O> HashMap<T, O> singleton(@NotNull T key, @Nullable O value) {
    HashMap<T, O> map = new HashMap<>();
    map.put(key, value);
    return map;
  }

  /**
   * Creates a map builder
   *
   * @param key with which the value is associated
   * @param value the value associated with the key
   * @param <K> the type of the key
   * @param <V> the type of the value
   * @return the map builder
   */
  @NotNull
  public static <K, V> MapBuilder<K, V> builder(@NotNull K key, @Nullable V value) {
    return new MapBuilder<>(singleton(key, value));
  }

  /**
   * Create a hashmap from strings separating the key and the value using a dot '.'. If there's no
   * dot it will be ignored
   *
   * @param strings to get the key and value
   * @return the map
   */
  public static HashMap<String, String> fromStringArray(@NotNull String... strings) {
    HashMap<String, String> map = new HashMap<>();
    for (String string : strings) {
      if (string.contains(".")) {
        String[] split = string.split("\\.");
        map.put(split[0], split[1]);
      }
    }
    return map;
  }

  /**
   * Create a hashmap from strings separating the key and the value using a dot '.'. If there's no
   * dot it will be ignored
   *
   * @param separator the char separator for the key and value
   * @param strings to get the key and value
   * @return the map
   */
  public static HashMap<String, String> fromStringArray(
      String separator, @NotNull String... strings) {
    HashMap<String, String> map = new HashMap<>();
    for (String string : strings) {
      if (string.contains(separator)) {
        String[] split = string.split(separator);
        map.put(split[0], split[1]);
      }
    }
    return map;
  }

  /**
   * Separates each element with a coma ',' and the key and value with a '.' also check {@link
   * Maps##fromStringArray(String...)}
   *
   * @param string to convert
   * @return the map
   */
  public static HashMap<String, String> fromString(@NotNull String string) {
    return fromStringArray(string.split(","));
  }

  /**
   * Puts all the map in a string that can be read using {@link Maps#fromString(String)}
   *
   * @param map to get as string
   * @return the built string
   */
  @NotNull
  public static String toString(HashMap<String, String> map) {
    StringBuilder builder = Strings.getBuilder();
    map.forEach((key, value) -> builder.append(key).append(".").append(value).append(","));
    return builder.toString();
  }
}
