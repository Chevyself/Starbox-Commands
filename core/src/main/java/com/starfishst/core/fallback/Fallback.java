package com.starfishst.core.fallback;

import com.starfishst.core.utils.Pagination;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** A class to handle errors in a more user friendly way */
public class Fallback {

  /** A list to store the errors messages */
  @NotNull private static final Pagination<String> errors = new Pagination<>(new ArrayList<>());

  /**
   * Add an error to the list
   *
   * @param message the message sent by the error
   */
  public static void addError(@NotNull String message) {
    if (!errors.getList().contains(message)) {
      errors.getList().add(message);
    }
  }

  /**
   * Get a page of errors
   *
   * @param page the page number to see
   * @param limit the limit of elements per page
   * @return the page of errors
   */
  public static List<String> getErrors(int page, int limit) {
    return errors.getPage(page, limit);
  }

  /**
   * Get the max page of errors
   *
   * @param limit the limit of elements per page
   * @return the max page number
   */
  public static int maxPage(int limit) {
    return errors.maxPage(limit);
  }

  /** Clear the list of errors */
  public static void clear() {
    errors.getList().clear();
  }

  /**
   * Get how many errors there is
   *
   * @return how many errors there is
   */
  public static int size() {
    return errors.getList().size();
  }

  /**
   * Check if there's errors
   *
   * @return true if there's no errors
   */
  public static boolean isEmpty() {
    return errors.getList().isEmpty();
  }

  /**
   * Get the pagination of errors
   *
   * @return the pagination of errors
   */
  @NotNull
  public static Pagination<String> getErrors() {
    return errors;
  }
}
