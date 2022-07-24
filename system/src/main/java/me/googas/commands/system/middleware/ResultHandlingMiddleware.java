package me.googas.commands.system.middleware;

import lombok.NonNull;
import me.googas.commands.result.StarboxResult;
import me.googas.commands.system.SystemResult;
import me.googas.commands.system.context.CommandContext;

public class ResultHandlingMiddleware implements SystemMiddleware {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof SystemResult) {
      result.getMessage().ifPresent(System.out::println);
    }
  }
}
