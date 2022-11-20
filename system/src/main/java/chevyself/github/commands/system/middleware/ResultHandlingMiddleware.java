package chevyself.github.commands.system.middleware;

import chevyself.github.commands.result.StarboxResult;
import chevyself.github.commands.system.SystemResult;
import chevyself.github.commands.system.context.CommandContext;
import lombok.NonNull;

public class ResultHandlingMiddleware implements SystemMiddleware {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof SystemResult) {
      result.getMessage().ifPresent(System.out::println);
    }
  }
}
