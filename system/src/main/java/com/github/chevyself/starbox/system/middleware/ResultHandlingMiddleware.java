package com.github.chevyself.starbox.system.middleware;

import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.system.SystemResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class ResultHandlingMiddleware implements SystemMiddleware {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof SystemResult) {
      result.getMessage().ifPresent(System.out::println);
    }
  }
}
