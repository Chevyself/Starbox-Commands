package com.starfishst.util;

import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Pagination;
import java.util.List;

/** Test for paginations */
public class PaginationTest {

  /**
   * Main
   *
   * @param args no args
   */
  public static void main(String[] args) {
    List<Integer> list = Lots.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
    Pagination<Integer> integerPagination = new Pagination<>(list);
    System.out.println(integerPagination.getIndex(3)); // should be 2
  }
}
