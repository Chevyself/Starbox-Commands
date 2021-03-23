package me.googas.commands.utility;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

/**
 * Static utils for maps
 *
 * @deprecated Probably removed when Minecraft requires Java 11
 */
public class Maps {

  /**
   * Create a map starting with a single property. Note that the map will be a {@link HashMap}
   *
   * @param key the key of the property
   * @param value the value of the property
   * @param <T> the type of the key
   * @param <O> the type of the value
   * @return the map with the starting key and value
   */
  @NonNull
  public static <T, O> Map<T, O> singleton(@NonNull T key, O value) {
    HashMap<T, O> map = new HashMap<>();
    map.put(key, value);
    return map;
  }

  /**
   * Creates a map builder. Note that this uses {@link #singleton(Object, Object)} to initialize the
   * map therefore the map is a {@link HashMap}
   *
   * @param key the key of the initial property
   * @param value the value of the initial property
   * @param <K> the type of the key
   * @param <V> the type of the value
   * @return the map builder with the starting key and value
   */
  @NonNull
  public static <K, V> MapBuilder<K, V> builder(@NonNull K key, V value) {
    return new MapBuilder<>(Maps.singleton(key, value));
  }

  /**
   * Creates a map builder without a defined type of values
   *
   * @param key the key of the initial property
   * @param value the value of the initial property
   * @param <K> the type of the key
   * @return the map builder with the starting key and value
   */
  @NonNull
  public static <K> MapBuilder<K, Object> objects(@NonNull K key, Object value) {
    return new MapBuilder<>(Maps.singleton(key, value));
  }

  /**
   * Create a map from strings separating the key and the value using a dot '.'. If there's no dot
   * it will be ignored and not added. This uses the method {@link #fromStringArray(String,
   * String...)}
   *
   * @param strings to get the key and value as follows "[key].[value]"
   * @return the map generated from the array it could be empty, it will not have null values
   */
  public static Map<String, String> fromStringArray(@NonNull String... strings) {
    return Maps.fromStringArray(".", strings);
  }

  /**
   * Create a map from an array of strings, each string will be separated to a key and a value with
   * the parameter separator as follows:
   *
   * <p>"[key][separator][value]"
   *
   * @see #fromStringArray(String...)
   *     <p>If the string does not contain the separator it will be ignored and note added to the
   *     map
   * @param separator the character separator for the key and value
   * @param strings the array of strings to separate the key and values
   * @return the create map from the array
   */
  public static Map<String, String> fromStringArray(String separator, @NonNull String... strings) {
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
   * Splits the string into an array of strings to use in {@link #fromStringArray(String...)} the
   * regex that splits the strings is just ","
   *
   * @param string to convert into an array and later to the map
   * @return the map that could be empty and never with null values
   */
  public static Map<String, String> fromString(@NonNull String string) {
    return Maps.fromStringArray(string.split(","));
  }

  /**
   * Puts all the map in a string that can be read using {@link Maps#fromString(String)}
   *
   * @param map to get as a string
   * @return the built string
   */
  @NonNull
  public static String toString(@NonNull Map<String, String> map) {
    StringBuilder builder = new StringBuilder();
    map.forEach((key, value) -> builder.append(key).append(".").append(value).append(","));
    return builder.toString();
  }
}
