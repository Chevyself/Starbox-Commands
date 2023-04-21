package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.bungee.CooldownManager;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.bungee.result.Result;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.Optional;
import lombok.NonNull;

/** Middleware to check and apply cooldown to commands. */
public class CooldownMiddleware implements BungeeMiddleware {

  @Override
  public @NonNull Optional<BungeeResult> next(@NonNull CommandContext context) {
    return context
        .getCommand()
        .getCooldownManager()
        .map(
            cooldown -> {
              if (cooldown.hasCooldown(context)) {
                return Result.of(
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
