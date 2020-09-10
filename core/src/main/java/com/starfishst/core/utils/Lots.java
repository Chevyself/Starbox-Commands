package com.starfishst.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Static utilities for groups of objects such as {@link List}, {@link java.lang.reflect.Array} or
 * {@link Set}
 */
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

  /**
   * Get an array from certain position from another one
   *
   * @param position the position to get the array from
   * @param array the array to get the new from
   * @return the new array with the objects from the array at a certain position
   */
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

  /**
   * Gives a pretty string of a collection
   *
   * @param collection the collection to give as string
   * @param <O> the type of the collection
   * @return a string given by the collection
   */
  @NotNull
  public static <O> String pretty(Collection<O> collection) {
    return collection.toString().replace("[", "").replace("]", "");
  }

  /**
   * Adds all the elements from another list that assert the predicate
   *
   * @param list the list to add the matching elements
   * @param toAdd the elements to test
   * @param bol the predicate to assert
   * @param <O> the type of the objects
   * @return the list that got the elements added
   */
  public static <O> List<O> addIf(@NotNull List<O> list, @NotNull List<O> toAdd, Predicate<O> bol) {
    list.addAll(toAdd.stream().filter(bol).collect(Collectors.toList()));
    return list;
  }

  /**
   * Create an immutable list
   *
   * @param objects the objects to put inside the list
   * @param <O> the type of objects inside the list
   * @return the immutable list
   */
  @SafeVarargs
  public static <O> List<O> inmutable(@NotNull O... objects) {
    return Collections.unmodifiableList(Lots.list(objects));
  }

  /**
   * Create a set of objects
   *
   * @param objects the objects to get as set
   * @param <O> the type of the objects
   * @return the created set of objects
   */
  @SafeVarargs
  @NotNull
  public static <O> Set<O> set(@NotNull O... objects) {
    return new HashSet<>(Lots.list(objects));
  }
}
