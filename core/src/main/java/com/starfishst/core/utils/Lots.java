package com.starfishst.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** Utils for {@link java.util.List} and {@link java.lang.reflect.Array} */
public class Lots {

  /**
   * Build an array of objects
   *
   * @param objects the objects to put in an array
   * @param <O> object
   * @return the array of objects
   */
  @SafeVarargs
  public static <O> @NotNull O[] array(@NotNull O... objects) {
    return objects;
  }

  /**
   * Build a list of objects
   *
   * @param objects the objects to put in the list
   * @param <O> object
   * @return the list of objects
   */
  @SafeVarargs
  @NotNull
  public static <O> List<O> list(@NotNull O... objects) {
    return new ArrayList<>(Arrays.asList(objects));
  }

  /**
   * Removes a element from an array and converts it into a {@link List}
   *
   * @param array the old array
   * @param position the position to remove
   * @param <O> {@link Object}
   * @return the new list with the removed element
   * @throws IllegalArgumentException if position is less than 0
   */
  public static <O> List<O> removeAndList(O[] array, int position) {
    if (position < 0) {
      throw new IllegalArgumentException("Position cannot be less than 0");
    } else {
      List<O> list = new ArrayList<>();
      for (int i = 0; i < array.length; i++) {
        if (i != position) {
          list.add(array[i]);
        }
      }

      return list;
    }
  }

  /**
   * Removes a element from an array
   *
   * @param array the old array
   * @param position the position to remove
   * @return the new array
   * @throws IllegalArgumentException if position is less than 0
   */
  public static String[] remove(String[] array, int position) {
    if (position < 0) {
      throw new IllegalArgumentException("Position cannot be less than 0");
    } else {
      String[] newArr = new String[array.length - 1];
      for (int i = 0; i < array.length; i++) {
        if (i < position) {
          newArr[i] = array[i];
        } else if (i > position) {
          newArr[i - 1] = array[i];
        }
      }

      return newArr;
    }
  }

  @NotNull
  public static String[] arrayFrom(int position, String[] array) {
    if (position < 0) {
      throw new IllegalArgumentException("Position cannot be less than 0");
    } else if (position > array.length) {
      throw new IllegalArgumentException("Position cannot be higher than array length");
    } else {
      String[] newArr = new String[array.length - position];
      int newArrPosition = 0;
      for (int i = position; i < array.length; i++) {
        newArr[newArrPosition] = array[i];
        newArrPosition++;
      }
      return newArr;
    }
  }
}
