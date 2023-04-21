package com.github.chevyself.starbox.system.middleware;

import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.system.CooldownManager;
import com.github.chevyself.starbox.system.Result;
import com.github.chevyself.starbox.system.SystemResult;
import com.github.chevyself.starbox.system.context.CommandContext;
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
