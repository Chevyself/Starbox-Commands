package com.starfishst.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A pagination objects allow you to have a list with some extra methods that allows pagination
 *
 * @param <O> the object that is being paginated
 */
public class Pagination<O> {

  /** The list of objects inside the pagination */
  @NotNull private final List<O> list;

  /**
   * Create an instance
   *
   * @param list the list to paginate
   */
  public Pagination(@NotNull List<O> list) {
    this.list = list;
  }

  /**
   * Get the max page number according to some limit
   *
   * @param limit the limit of elements per page
   * @return the max page number
   */
  public int maxPage(int limit) {
    return (list.size() + limit - 1) / limit;
  }

  /**
   * Get a page using a page number and a limit the page cannot be less than 1 and higher than
   * {@link Pagination#maxPage(int)}
   *
   * @param page the page to see
   * @param limit the limit of elements in each page
   * @return the page
   */
  public List<O> getPage(int page, int limit) {
    validatePage(page, limit);
    List<O> newPage = new ArrayList<>();
    int skip = (page - 1) * limit;
    int i = skip;
    while (i < skip + limit && i < list.size()) {
      newPage.add(list.get(i));
      i++;
    }
    return newPage;
  }

  /**
   * Validates if a page number is correct Checks if is higher than 0 Checks if it is less or the
   * {@link Pagination#maxPage(int)}
   *
   * @param page the page to validate
   * @param limit the limit of elements by page
   */
  private void validatePage(int page, int limit) {
    int maxPage = maxPage(limit);
    Validate.assertTrue(page > 0, "Page cannot be less than 1");
    Validate.assertTrue(page <= maxPage, "Page cannot be higher than the maximum page: " + maxPage);
  }

  /**
   * Get the index of an object in a list or -1 if the list does not contain the object
   *
   * @param o the object to get the index of
   * @return the index of the object
   */
  public int getIndex(@NotNull O o) {
    int index = 0;
    for (int i = 0; i < list.size(); i++) {
      if (o.equals(list.get(i))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Get the list that is being paginated
   *
   * @return the list that is being paginated
   */
  @NotNull
  public List<O> getList() {
    return list;
  }
}
