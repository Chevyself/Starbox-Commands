package chevyself.github.commands.jda.middleware;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.result.Result;
import java.util.Optional;
import lombok.NonNull;

/** This is an implementation of {@link Middleware} for the JDA module. */
public interface JdaMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<Result> next(@NonNull CommandContext context);
}
