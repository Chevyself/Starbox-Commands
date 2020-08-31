package com.starfishst.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/** Static utilities for randomization */
public class RandomUtils {

  /** The java Random instance */
  @NotNull private static final Random random = new Random();
  /** Upper case letters. Used for random strings */
  @NotNull private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  /** Lower case letters. Used for random strings */
  @NotNull private static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  /** Numbers inside a string. Used for random strings */
  @NotNull private static final String NUMBERS = "1234567890";

  /**
   * Create a random string with the provided characters. It will loop getting random characters
   * from the charsString until it reaches the provided length
   *
   * @param charsString the provided characters
   * @param length the length of the new string
   * @return the randomly generated string
   */
  public static String nextString(@NotNull String charsString, int length) {
    char[] text = new char[length];
    for (int i = 0; i < length; i++) {
      text[i] = charsString.charAt(random.nextInt(charsString.length()));
    }
    return new String(text);
  }

  /**
   * Create a random string with letters and numbers using {@link #nextString(String, int)} and
   * providing {@link #LETTERS}, {@link #LOWER_LETTERS} and {@link #NUMBERS}
   *
   * @param length the length of the new string
   * @return the randomly generated string
   */
  public static String nextString(int length) {
    return nextString(LETTERS + LOWER_LETTERS + NUMBERS, length);
  }

  /**
   * Get an integer inside a bound. The bound is created as follows:
   *
   * <p>i = ((max - min) + 1) + min
   *
   * @param min the minimum number of the bound
   * @param max the maximum number of the bound
   * @return the random generated integer inside the bounds
   */
  public static int nextInt(int min, int max) {
    return random.nextInt(((max - min) + 1) + min);
  }

  /**
   * Get a double inside a bound. The bound is created as follows:
   *
   * <p>i = min + ((1 + max - min) * {@link Math#random()});
   *
   * @param min the minimum double of the bound
   * @param max the maximum double of the bound
   * @return the random generated double inside the bounds
   */
  public static double nextDouble(double min, double max) {
    if (max < min) {
      return nextDouble(max, min);
    } else {
      return min + ((1 + max - min) * Math.random());
    }
  }

  /**
   * This method uses {@link #nextDouble(double, double)} but {@link Math#floor(double)} the result
   *
   * @param min the minimum double of the bound
   * @param max the maximum double of the bound
   * @return the random generated and {@link Math#floor(double)} double inside the bound.
   */
  public static double nextDoubleFloor(double min, double max) {
    return Math.floor(nextDouble(min, max));
  }

  /**
   * Create a random string only using letters using {@link #nextString(String, int)} and providing
   * {@link #LETTERS} and {@link #LOWER_LETTERS}
   *
   * @param length the length of the new string
   * @return the randomly generated string
   */
  public static String nextStringLetters(int length) {
    return nextString(LETTERS + LOWER_LETTERS, length);
  }

  /**
   * Create a random string only using {@link #nextString(String, int)} and providing {@link
   * #LETTERS}
   *
   * @param length the length of the new string
   * @return the randomly generated string
   */
  public static String nextStringUpper(int length) {
    return nextString(LETTERS, length);
  }

  /**
   * Create a random string only using {@link #nextString(String, int)} and providing {@link
   * #LOWER_LETTERS}
   *
   * @param length the length of the new string
   * @return the randomly generated string
   */
  public static String nextStringLower(int length) {
    return nextString(LOWER_LETTERS, length);
  }

  /**
   * Get a random element from a set. It will convert the set into an Array List and the get a
   * random integer inside the bounds of the set size.
   *
   * @param set the set to get the element
   * @param <O> the type of the elements from the set
   * @return the randomly selected element
   * @throws IllegalArgumentException if the set is empty
   */
  @NotNull
  public static <O> O getRandom(@NotNull Set<O> set) {
    if (set.isEmpty()) {
      throw new IllegalArgumentException("The input is empty");
    }
    return new ArrayList<>(set).get(random.nextInt(set.size()));
  }

  /**
   * Get a random object inside a list. It will get a random integer inside the bounds of the array
   * list size.
   *
   * @param list the list to get the element
   * @param <O> the type of the elements from the list
   * @return the randomly selected element
   * @throws IllegalArgumentException if the array list is empty
   */
  @NotNull
  public static <O> O getRandom(@NotNull List<O> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty!");
    }
    return list.get(random.nextInt(list.size()));
  }

  /**
   * Get the amount of randomly selected elements. Using {@link #getRandom(List)} getting elements
   * to add in a new {@link ArrayList}
   *
   * @param list the list to get the elements
   * @param amount the amount of elements to get
   * @param <O> the type of the elements from the list
   * @return the randomly selected elements
   * @throws IllegalArgumentException if the provided list is empty or the amount is bigger than the
   *     elements inside the list
   */
  @NotNull
  public static <O> List<O> getRandom(@NotNull List<O> list, int amount) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty!");
    } else if (amount > list.size()) {
      throw new IllegalArgumentException("The amount is bigger than the size of the list");
    }
    List<O> newList = new ArrayList<>(amount);
    O toAdd = getRandom(list);
    while (newList.size() != amount) {
      while (newList.contains(toAdd)) {
        toAdd = getRandom(list);
      }
      newList.add(toAdd);
    }
    return newList;
  }

  /**
   * Get the {@link Random} object used for the generation of randomness for this utility
   *
   * @return the object
   */
  @NotNull
  public static Random getRandom() {
    return random;
  }
}
