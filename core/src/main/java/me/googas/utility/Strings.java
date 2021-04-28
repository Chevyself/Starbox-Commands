package me.googas.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;

/** Static utilities for {@link String} */
public class Strings {

  /**
   * This method is made to save resources from {@link #format(String, Map)}, {@link #format(String,
   * Object...)} and {@link #format(String, MapBuilder)} to not go in a loop. In case that the
   * message is null it will just give an string with the characters "Null"
   *
   * @param message the message to format
   * @return "Null" if the message is null else the message
   */
  @NonNull
  public static String format(String message) {
    return message == null ? "Null" : message;
  }

  /**
   * Build a message which has place holders in the next fashion:
   *
   * <p>"This message has a {0}"
   *
   * <p>"{0}" is the placeholder. It has to start from 0 and then scale up. The 0 represents the
   * index from the objects array. The placeholder will be replaced with the {@link
   * Object#toString()}
   *
   * @param message the message to format
   * @param strings the objects that will replace the placeholders
   * @return the formatted message
   */
  @NonNull
  public static String format(String message, Object... strings) {
    if (message != null) {
      for (int i = 0; i < strings.length; i++) {
        message =
            message.replace("{" + i + "}", strings[i] == null ? "Null" : strings[i].toString());
      }
    } else {
      message = "Null";
    }
    return message;
  }

  /**
   * Build a message using more readable placeholders. Instead of using a method such as {@link
   * #format(String, Object...)} this uses a map with the placeholder and the given object to
   * replace it:
   *
   * <p>"This message has a %placeholder%"
   *
   * <p>"%placeholder%" is the placeholder that will be replaced with the object that it was given
   * in the map
   *
   * @param message the message to format
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the formatted message
   */
  @NonNull
  public static String format(String message, @NonNull Map<String, String> placeholders) {
    if (message == null) return "Null";
    AtomicReference<String> atomicMessage = new AtomicReference<>(message);
    for (String placeholder : placeholders.keySet()) {
      String value = placeholders.get(placeholder);
      if (value != null) {
        atomicMessage.set(atomicMessage.get().replace("%" + placeholder + "%", value));
      } else {
        atomicMessage.set(atomicMessage.get().replace("%" + placeholder + "%", "null"));
      }
    }
    return atomicMessage.get();
  }

  /**
   * This method is the same as {@link #format(String, Map)} but using the {@link MapBuilder} to
   * give an option of easier to make map
   *
   * @param message the message to format
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the formatted message
   */
  public static String format(String message, @NonNull MapBuilder<String, String> placeholders) {
    return Strings.format(message, placeholders.build());
  }

  /**
   * Build a {@link String} using an array of those. If you have the array: ["Hello", "world"] the
   * resulting string will be: "Hello world"
   *
   * @param strings the array strings to build
   * @return the built string using the array
   */
  @NonNull
  public static String fromArray(@NonNull String[] strings) {
    StringBuilder builder = new StringBuilder();

    for (String string : strings) {
      builder.append(string).append(" ");
    }
    if (builder.length() >= 1) {
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.toString();
  }

  /**
   * Copy the matching strings from a list to a new one:
   *
   * <p>If you have the next string to match: "Hello" and your list contains the elements:
   *
   * <p>["Hello world", "Hello everyone", "What's up", "How's it going"]
   *
   * <p>This will copy the elements:
   *
   * <p>["Hello world", "Hello everyone"]
   *
   * @param toMatch The string to match the elements
   * @param list the list of the elements to match
   * @return the list with the matching elements
   */
  @NonNull
  public static List<String> copyPartials(@NonNull String toMatch, @NonNull List<String> list) {
    List<String> matching = new ArrayList<>();
    return Series.addIf(
        matching, list, string -> string.toLowerCase().startsWith(toMatch.toLowerCase()));
  }

  /**
   * Builds the aliases usage from a collection of Strings.
   *
   * If the collection looks like: ["hello", "world", "foo"]
   *
   * The returning string will be: hello|world|foo
   *
   * @param aliases the collection of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the collection is empty
   */
  @NonNull
  public static String buildUsageAliases(@NonNull Collection<String> aliases) {
    if (aliases.isEmpty()) throw new IllegalArgumentException("Aliases collection may not be empty!");
    StringBuilder builder = new StringBuilder();
    for (String alias : aliases) {
      builder.append(alias).append("|");
    }
    return builder.deleteCharAt(builder.length() - 1).toString();
  }

  /**
   * Builds the aliases usage from an array of Strings
   *
   * @see #buildUsageAliases(Collection)
   * @param aliases the array of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the array is empty
   */
  public static String buildUsageAliases(@NonNull String... aliases) {
    return buildUsageAliases(Arrays.asList(aliases));
  }
}
