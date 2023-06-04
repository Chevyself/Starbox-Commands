package me.googas.tests;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlagsTest {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Pattern test = Pattern.compile("((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\")|\\S+)");
    while (true) {
      String command =
          "The power of \"Jesus Christ\"";
      String[] split = command.split(" +");
      String line = scanner.nextLine();
      // System.out.println(Arrays.toString(split));
      if (line.equals("exit")) break;
      // Pattern pattern = Pattern.compile("(-[a-zA-Z]+(=[a-zA-Z0-9]+)?\\s*)+");
      // Pattern pattern = Pattern.compile("(-[a-zA-Z]+(=[a-zA-Z0-9]+)?\\s*)+");
      // Pattern pattern = Pattern.compile(line);
      Matcher matcher = test.matcher(line);
      while (matcher.find()) {
        // System.out.println(matcher.group());
        // String name = matcher.group(2);
        // String value = matcher.group(4);
        System.out.println(matcher.group());
        /*
        for (int i = 0; i <= matcher.groupCount(); i++) {
          System.out.println(matcher.group());
           System.out.println(i + ": " + matcher.group(i));
        }
         */
        // System.out.println(matcher.group());
        // System.out.println("Name: " + name);
        // System.out.println("Value: " + value);
      }
    }
  }
}
