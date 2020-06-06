package com.starfishst.test.random;

import com.starfishst.core.utils.RandomUtils;
import java.util.Scanner;

public class RandomUtilsTest {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      if (scanner.hasNext()) {
        int i = scanner.nextInt();
        if (i == 0) {
          break;
        } else {
          System.out.println(RandomUtils.nextStringLetters(i));
        }
      }
    }
  }
}
