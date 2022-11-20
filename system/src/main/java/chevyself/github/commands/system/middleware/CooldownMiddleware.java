package chevyself.github.commands.system.middleware;

import chevyself.github.commands.result.StarboxResult;
import chevyself.github.commands.system.CooldownManager;
import chevyself.github.commands.system.Result;
import chevyself.github.commands.system.SystemResult;
import chevyself.github.commands.system.context.CommandContext;
import java.util.Optional;
import lombok.NonNull;

/** Middleware to apply cooldown to commands. */
public class CooldownMiddleware implements SystemMiddleware {

  @Override
  public @NonNull Optional<SystemResult> next(@NonNull CommandContext context) {
    Optional<CooldownManager> optional = context.getCommand().getCooldownManager();
    return optional.map(
        cooldown -> {
          if (cooldown.hasCooldown(context)) {
            return new Result(
                context.getMessagesProvider().cooldown(context, cooldown.getTimeLeft(context)));
          }
          return null;
        });
  }

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    Optional<CooldownManager> optional = context.getCommand().getCooldownManager();
    if (optional.isPresent() && result.isCooldown()) {
      optional.get().refresh(context);
    }
  }
}
