package me.googas.commands.system.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.result.StarboxResult;
import me.googas.commands.system.CooldownManager;
import me.googas.commands.system.Result;
import me.googas.commands.system.context.CommandContext;

/** Middleware to apply cooldown to commands. */
public class CooldownMiddleware implements SystemMiddleware {

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
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
