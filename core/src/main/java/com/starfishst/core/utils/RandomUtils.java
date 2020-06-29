package com.starfishst.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/** Utilities for randomization */
public class RandomUtils {

  /** The random instance */
  @NotNull private static final Random random = new Random();
  /** Upper case letters */
  @NotNull private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  /** Lower case letters */
  @NotNull private static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  /** Numbers inside a string */
  @NotNull private static final String NUMBERS = "1234567890";

  /**
   * Create a random string with the provided characters
   *
   * @param charsString the provided characters
   * @param length of the new string
   * @return the string
   */
  public static String nextString(@NotNull String charsString, int length) {
    char[] text = new char[length];
    for (int i = 0; i < length; i++) {
      text[i] = charsString.charAt(random.nextInt(charsString.length()));
    }
    return new String(text);
  }

  /**
   * Create a random string with letters and numbers
   *
   * @param length of the new string
   * @return the string
   */
  public static String nextString(int length) {
    return nextString(LETTERS + LOWER_LETTERS + NUMBERS, length);
  }

  /**
   * Create a random string only using letters
   *
   * @param length of the new string
   * @return the string
   */
  public static String nextStringLetters(int length) {
    return nextString(LETTERS + LOWER_LETTERS, length);
  }

  /**
   * Create a random string only using upper case letters
   *
   * @param length of the new string
   * @return the string
   */
  public static String nextStringUpper(int length) {
    return nextString(LETTERS, length);
  }

  /**
   * Create a random string only using lower case letters
   *
   * @param length of the new string
   * @return the string
   */
  public static String nextStringLower(int length) {
    return nextString(LOWER_LETTERS, length);
  }

  /**
   * Get a random element from a set
   *
   * @param set the set to get the element
   * @param <O> the type of the elements
   * @return the random element
   */
  @NotNull
  public static <O> O getRandom(@NotNull Set<O> set) {
    return new ArrayList<>(set).get(random.nextInt(set.size()));
  }

  /**
   * Get the random java util object
   *
   * @return the random util object
   */
  @NotNull
  public static Random getRandom() {
    return random;
  }

  /**
   * Get a random object inside a list
   *
   * @param list the list to get the random object from
   * @param <O> the type of the elements inside the list
   * @return the random selected object
   * @throws IllegalArgumentException if the parameter list is empty
   */
  @NotNull
  public static <O> O getRandom(@NotNull List<O> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty!");
    }
    return list.get(random.nextInt(list.size()));
  }
}
