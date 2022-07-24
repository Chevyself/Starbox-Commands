package me.googas.commands.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;

/** Static utilities for {@link String}. */
public class Strings {

  /** A string only containing uppercase letters. */
  @NonNull public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  /** A string only containing lowercase letters. */
  @NonNull public static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  /** A char array containing both uppercase and lowercase letters. */
  public static final char[] LETTERS =
      (Strings.UPPERCASE_LETTERS + Strings.LOWERCASE_LETTERS).toCharArray();
  /** A char array containing uppercase letters. */
  public static final char[] UPPERCASE = Strings.UPPERCASE_LETTERS.toCharArray();
  /** A char array containing lowercase letters. */
  public static final char[] LOWERCASE = Strings.LOWERCASE_LETTERS.toCharArray();

  /**
   * This method is made to save resources from {@link #format(String, Map)}, {@link #format(String,
   * Object...)} to not go in a loop. In case that the message is null it will just give a string
   * with the characters "Null"
   *
   * @param message the message to format
   * @return "Null" if the message is null else the message
   */
  @NonNull
  public static String format(String message) {
    return message == null ? "Null" : message;
  }

  /**
   * Build a message which has placeholders in the next fashion:
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
    if (message == null) {
      return "Null";
    }
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
   * Build a {@link String} using an array of those. If you have the array: ["Hello", "world"] the
   * resulting string will be: "Hello world"
   *
   * @param strings the array strings to build
   * @return the built string using the array
   */
  @NonNull
  public static String join(@NonNull String[] strings) {
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
   * Copy the matching strings from a list to a new one.
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
    for (String string : list) {
      if (string.toLowerCase().startsWith(toMatch.toLowerCase())) {
        matching.add(string);
      }
    }
    return matching;
  }

  /**
   * Builds the aliases' usage from a collection of Strings.
   *
   * <p>If the collection looks like: ["hello", "world", "foo"]
   *
   * <p>The returning string will be: hello|world|foo
   *
   * @param aliases the collection of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the collection is empty
   */
  @NonNull
  public static String buildUsageAliases(@NonNull Collection<String> aliases) {
    if (aliases.isEmpty()) {
      throw new IllegalArgumentException("Aliases collection may not be empty!");
    }
    StringBuilder builder = new StringBuilder();
    for (String alias : aliases) {
      builder.append(alias).append("|");
    }
    return builder.deleteCharAt(builder.length() - 1).toString();
  }

  /**
   * Builds the aliases' usage from an array of Strings.
   *
   * @param aliases the array of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the array is empty
   * @see #buildUsageAliases(Collection)
   */
  public static String buildUsageAliases(@NonNull String... aliases) {
    return Strings.buildUsageAliases(Arrays.asList(aliases));
  }

  /**
   * Divides the given string to different strings of the given length.
   *
   * @param string the string to divide
   * @param length the length that each string must be
   * @return the list containing each string
   */
  @NonNull
  public static List<String> divide(@NonNull String string, int length) {
    List<String> split = new ArrayList<>();
    while (string.length() > length) {
      String substring = string.substring(0, length);
      string = string.substring(length);
      split.add(substring);
    }
    if (!string.isEmpty()) {
      split.add(string);
    }
    return split;
  }

  /**
   * Get the similarity between two string.
   *
   * @param longer the first string
   * @param shorter the second string
   * @return the similarity between the two strings 0f being no similarity and 1f being that the two
   *     strings are the same
   */
  public static float similarity(@NonNull String longer, @NonNull String shorter) {
    String temp = longer;
    if (longer.length() < shorter.length()) {
      longer = shorter;
      shorter = temp;
    }
    float longerLength = longer.length();
    if (longerLength == 0) {
      return 1f;
    }
    return (longerLength - Strings.editDistance(longer, shorter)) / longerLength;
  }

  /**
   * Get the similarity between two strings ignoring casing.
   *
   * @param longer the first string
   * @param shorter the second string
   * @return the similarity between the two strings 0f being no similarity and 1f being that the two
   *     strings are the same
   * @see #similarity(String, String)
   */
  public static float similarityIgnoreCase(@NonNull String longer, @NonNull String shorter) {
    return Strings.similarity(longer.toLowerCase(), shorter.toLowerCase());
  }

  /**
   * Make a pretty strings from a {@link Collection}. This will check if the collection is empty if
   * it is the case, the parameter empty will be returned else this will simply {@link
   * Object#toString()} the collection and replace the characters '[]' to nothing
   *
   * @param collection the collection to pretty
   * @param empty the string in case the collection is empty
   * @return the pretty collection string
   */
  @NonNull
  public static String pretty(@NonNull Collection<?> collection, @NonNull String empty) {
    return collection.isEmpty() ? empty : collection.toString().replace("[", "").replace("]", "");
  }

  /**
   * Make a pretty string from a {@link Collection}. If the collection is empty this will use '[]'
   *
   * @param collection the collection to pretty
   * @return the pretty collection string
   * @see #pretty(Collection, String)
   */
  @NonNull
  public static String pretty(@NonNull Collection<?> collection) {
    return Strings.pretty(collection, "[]");
  }

  /**
   * Make a pretty string from an array. This will check if the array is empty if it is the case,
   * the parameter empty will be returned else this will simple {@link Arrays#toString(int[])} the
   * array and replace the characters '[]' to nothing
   *
   * @param empty the string in case the array is empty
   * @param objects the array of object to pretty
   * @return the pretty array string
   */
  @NonNull
  public static String pretty(@NonNull String empty, @NonNull Object... objects) {
    return objects.length == 0 ? empty : Arrays.toString(objects);
  }

  /**
   * Make a pretty string from an array. If the collection is empty this will use '[]'
   *
   * @param objects the array of object to pretty
   * @return the pretty array string
   * @see #pretty(String, Object...)
   */
  @NonNull
  public static String pretty(@NonNull Object... objects) {
    return Strings.pretty("[]", objects);
  }

  /**
   * Generate a random strings with certain characters.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @param chars the characters that may be used in the string
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length, char... chars) {
    StringBuilder builder = new StringBuilder();
    while (builder.length() < length) {
      builder.append(chars[random.nextInt(chars.length)]);
    }
    return builder.toString();
  }

  /**
   * Generate a random string only using {@link #UPPERCASE} and {@link #LOWERCASE}.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length) {
    return Strings.nextString(random, length, Strings.LETTERS);
  }

  /**
   * Generate a random string using a {@link Charset}.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @param charset the charset of the characters that the string may contain
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length, @NonNull Charset charset) {
    byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return new String(bytes, charset);
  }

  /**
   * Groups an iteration of strings. Grouping means that it will check for quotation marks, the strings that
   * are inside those will be grouped as their own. From an array as:
   *
   * ["Hi!", "\"How", "is", "it", "going?\"", "Good"]
   *
   * The groups will be:
   *
   * ["Hi!", "How is it going?", "Good"]
   *
   * @param strings the strings to group
   * @return the list of grouped strings
   */
  @NonNull
  public static List<JoinedString> group(@NonNull Iterable<String> strings) {
    List<JoinedString> group = new ArrayList<>();
    boolean building = false;
    StringBuilder builder = new StringBuilder();
    int index = 0;
    for (String string : strings) {
      String toAppend = null;
      if (!building && string.startsWith("\"") && (string.length() > 1 || string.endsWith("\""))) {
        building = true;
        builder.append(string).append(" ");
      } else if (building && string.endsWith("\"")) {
        building = false;
        builder.append(string);
        index++;
        toAppend = builder.toString();
        builder.setLength(0);
      } else if (building) {
        builder.append(string).append(" ");
        index++;
      } else {
        toAppend = string;
      }
      if (toAppend != null) {
        group.add(new JoinedString(Strings.removeQuotations(toAppend), index));
        index = 0;
      }
    }
    if (building) {
      group.add(new JoinedString(builder.toString(), index));
    }
    return group;
  }

  /**
   * Groups an array of strings.
   *
   * @see #group(Iterable)
   * @param strings the strings to group
   * @return the list of grouped strings
   */
  @NonNull
  public static List<JoinedString> group(@NonNull String... strings) {
    return Strings.group(Arrays.asList(strings));
  }

  /**
   * Removes the starting and ending quotation marks from a string.
   *
   * <p>From: "Hello how are you" To: Hello how are you
   *
   * @param string the string to remove the quotation marks
   * @return the string
   */
  @NonNull
  public static String removeQuotations(@NonNull String string) {
    return (string.length() > 1 && string.startsWith("\"") && string.endsWith("\""))
        ? string.substring(1, string.length() - 1)
        : string;
  }

  private static int editDistance(String longer, String shorter) {
    longer = longer.toLowerCase();
    shorter = shorter.toLowerCase();
    int[] costs = new int[shorter.length() + 1];
    for (int i = 0; i <= longer.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= shorter.length(); j++) {
        if (i == 0) {
          costs[j] = j;
        } else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (longer.charAt(i - 1) != shorter.charAt(j - 1)) {
              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
            }
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0) {
        costs[shorter.length()] = lastValue;
      }
    }
    return costs[shorter.length()];
  }
}
