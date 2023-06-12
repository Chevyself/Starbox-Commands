package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.bukkit.CooldownManager;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.result.Result;
import java.util.Optional;
import lombok.NonNull;

/** Middleware to check and apply cooldown to commands. */
public class CooldownMiddleware implements BukkitMiddleware {

  @Override
  public @NonNull Optional<BukkitResult> next(@NonNull CommandContext context) {
    return context
        .getCommand()
        .getCooldownManager()
        .map(
            cooldown -> {
              if (cooldown.hasCooldown(context)) {
                return com.github.chevyself.starbox.bukkit.result.Result.of(
                    context.getMessagesProvider().cooldown(context, cooldown.getTimeLeft(context)));
              }
              return null;
            });
  }

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    Optional<CooldownManager> optional = context.getCommand().getCooldownManager();
    if (optional.isPresent() && result.isCooldown()) {
      optional.get().refresh(context);
    }
  }
}
