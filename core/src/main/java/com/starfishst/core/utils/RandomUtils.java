package com.starfishst.core.utils;

import java.util.Random;
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
}
