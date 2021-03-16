package me.googas.commands.utility;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;

/** Static utilities for {@link String} */
public class Strings {

  /**
   * This method is made to save resources from {@link #build(String, Map)}, {@link #build(String,
   * Object...)} and {@link #build(String, MapBuilder)} to not go in a loop. In case that the
   * message is null it will just give an string with the characters "Null"
   *
   * @param message the message to build
   * @return "Null" if the message is null else the message
   */
  @NonNull
  public static String build(String message) {
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
   * @param message the message to build
   * @param strings the object that will replace the placeholders
   * @return the built message
   */
  @NonNull
  public static String build(String message, Object... strings) {
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
   * #build(String, Object...)} this uses a map with the placeholder and the given object to replace
   * it:
   *
   * <p>"This message has a %placeholder%"
   *
   * <p>"%placeholder%" is the placeholder that will be replaced with the object that it was given.
   *
   * @param message the message to build
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the built message
   */
  @NonNull
  public static String build(String message, @NonNull Map<String, String> placeholders) {
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
   * This method is the same as {@link #build(String, Map)} but using the {@link MapBuilder} to give
   * an option of easier to make map
   *
   * @param message the message to build
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require * to have the character "%" and the value is another string
   * @return the built message
   */
  public static String build(String message, @NonNull MapBuilder<String, String> placeholders) {
    return Strings.build(message, placeholders.build());
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
}
