package me.googas.commands.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.NonNull;

/**
 * Static utilities for {@link java.util.Collection} and arrays. It is called Series as it is a
 * synonym for collection
 */
public class Series {

  /**
   * Copy an array from the given position.
   *
   * <p>If you have the next array: [1, 2, 3]
   *
   * <p>And you use this method with the position 1 the array will result in: [2, 3]
   *
   * @deprecated use {@link java.util.Arrays#copyOfRange(Object[], int, int)}
   * @param position the position to get the array from
   * @param array the array to get the new array from
   * @return the new array copied from the position of the parameter array
   * @throws IllegalArgumentException if the position is less than 0 or if the position is higher
   *     than the length of the array
   */
  @NonNull
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

  /**
   * Remove an element from and array and convert it to a list.
   *
   * <p>If you have the array: [1, 2, 3] and you wish to remove the 2 you will use the position 1
   * and you will end up with the list: [1, 3]
   *
   * @deprecated use {@link java.util.Arrays#copyOfRange(Object[], int, int)} and {@link java.util.Arrays#asList(Object[])}
   * @param array the array to remove the element and convert into a list
   * @param position the position to remove the element from
   * @param <O> the type of the objects in the array
   * @return the converted list from the array
   * @throws IllegalArgumentException if the position is less than 0
   */
  @NonNull
  public static <O> List<O> removeAndList(@NonNull O[] array, int position) {
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
   * Copy the elements from a list to another if those match the predicate
   *
   * <p>If you have the list [1, 2, 3, 4, 5, 6, 7, 8, 9] and you want to copy if the number is > 5
   * the elements added to the list are [6, 7, 8, 9]
   *
   * @param list the list to paste the values to
   * @param toAdd the list to copy the values from
   * @param bol the predicate to check the values to be added
   * @param <O> the type of elements inside the list
   * @return the list instance
   */
  @NonNull
  public static <O> List<O> addIf(
      @NonNull List<O> list, @NonNull List<O> toAdd, @NonNull Predicate<O> bol) {
    for (O object : toAdd) {
      if (bol.test(object)) {
        list.add(object);
      }
    }
    return list;
  }

  /**
   * Remove an element from an array.
   *
   * <p>If you have the array [1, 2, 3, 4, 5] and you use this method with the position: 3 the
   * element '4' will be removed resulting in the array being: [1, 2, 3, 5]
   *
   * @param array the array to remove the element from
   * @param position the position to remove the element from
   * @return the new array
   * @throws IllegalArgumentException if the position is less than 0
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
}
