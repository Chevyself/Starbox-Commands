package me.googas;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.MiddlewareParser;

public class MiddlewareParserTest {

  public static void main(String[] args) {
    MiddlewareParser<StarboxCommandContext> parser =
        new MiddlewareParser<>("com.github.chevyself.starbox.system.middleware");
    System.out.println(parser.parse());
  }
}
