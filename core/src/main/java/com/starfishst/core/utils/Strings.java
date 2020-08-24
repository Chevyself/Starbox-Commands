package com.starfishst.core.utils;

import com.starfishst.core.utils.maps.MapBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Static utilities for strings */
public class Strings {

  /** The string builder to some string building utilities */
  @NotNull private static final StringBuilder builder = new StringBuilder();

  /**
   * This method is made to save resources from {@link #buildMessage(String, HashMap)}, {@link
   * #buildMessage(String, Object...)} and {@link #buildMessage(String, MapBuilder)} to not go in a
   * loop. In case that the message is null it will just give an string with the characters "Null"
   *
   * @param message the message to build
   * @return "Null" if the message is null else the message
   */
  @NotNull
  public static String buildMessage(@Nullable String message) {
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
  @NotNull
  public static String buildMessage(@Nullable String message, Object... strings) {
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
   * Builds a String from an array of strings. This method will make a loop for each string
   * appending them in a string builder which will append a " " to the end of each string
   *
   * @param strings the string array to build the message
   * @return a brand new string made with the array
   */
  @NotNull
  public static String fromArray(@NotNull String[] strings) {
    StringBuilder builder = Strings.getBuilder();

    for (String string : strings) {
      builder.append(string).append(" ");
    }
    if (builder.length() >= 1) {
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.toString();
  }

  /**
   * Build a message using more readable placeholders. Instead of using a method such as {@link
   * #buildMessage(String, Object...)} this uses a map with the placeholder and the given object to
   * replace it:
   *
   * <p>"This message has a %placeholder%"
   *
   * <p>"%placeholder%" is the placeholder that will be replaced with the object that it was given.
   *
   * @param message the message to build
   * @param placeHolders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the built message
   */
  @NotNull
  public static String buildMessage(
      @Nullable String message, @NotNull HashMap<String, String> placeHolders) {
    if (message == null) return "Null";
    Atomic<String> atomicMessage = new Atomic<>(message);
    for (String placeholder : placeHolders.keySet()) {
      String value = placeHolders.get(placeholder);
      if (value != null) {
        atomicMessage.set(atomicMessage.get().replace("%" + placeholder + "%", value));
      } else {
        atomicMessage.set(atomicMessage.get().replace("%" + placeholder + "%", "null"));
      }
    }
    return atomicMessage.get();
  }

  /**
   * This method is the same as {@link #buildMessage(String, HashMap)} but using the {@link
   * MapBuilder} to give an option of easier to make map
   *
   * @param message the message to build
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require * to have the character "%" and the value is another string
   * @return the built message
   */
  public static String buildMessage(
      @Nullable String message, @NotNull MapBuilder<String, String> placeholders) {
    return buildMessage(message, placeholders.build());
  }

  /**
   * Missing method in {@link String}. Looks if a string contains a certain one ignoring casing
   *
   * @param string the string to look if is inside another string
   * @param search the another string to check if the string is inside of it
   * @return true if its contained else false;
   */
  public static boolean containsIgnoreCase(@NotNull String string, @NotNull String search) {
    final int length = search.length();
    if (length == 0) {
      return true;
    } else {
      for (int i = string.length() - length; i >= 0; i--) {
        if (string.regionMatches(true, i, search, 0, length)) return true;
      }
      return false;
    }
  }

  /**
   * Copies the strings that partially match in another array list of strings. This will check the
   * strings inside the list that start with the string to match
   *
   * @param list the list to get the strings to partially match
   * @param toMatch the string to partially match
   * @return the list with matching strings
   */
  @NotNull
  public static List<String> copyPartials(@NotNull String toMatch, @NotNull List<String> list) {
    List<String> matching = new ArrayList<>();
    return Lots.addIf(
        matching, list, string -> string.toLowerCase().startsWith(toMatch.toLowerCase()));
  }

  /**
   * Get a clear instance of {@link StringBuilder}. Clear instance means that it does have a length
   * of 0
   *
   * @return the clear instance of the string builder
   */
  public static StringBuilder getBuilder() {
    Strings.builder.setLength(0);
    return Strings.builder;
  }
}
