package chevyself.github.commands.bungee.middleware;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.result.BungeeResult;
import java.util.Optional;
import lombok.NonNull;

/** Middleware implementation for the 'Bungee' module. */
public interface BungeeMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<BungeeResult> next(@NonNull CommandContext context);
}
