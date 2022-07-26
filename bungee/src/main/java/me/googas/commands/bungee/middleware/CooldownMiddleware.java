package me.googas.commands.bungee.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.bungee.CooldownManager;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.BungeeResult;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.result.StarboxResult;

/** Middleware to check and apply cooldown to commands. */
public class CooldownMiddleware implements BungeeMiddleware {

  @Override
  public @NonNull Optional<BungeeResult> next(@NonNull CommandContext context) {
    return context.getCommand().getCooldownManager().map(
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
