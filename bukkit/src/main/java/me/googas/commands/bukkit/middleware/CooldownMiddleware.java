package me.googas.commands.bukkit.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.bukkit.CooldownManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.result.StarboxResult;

/** Middleware to check and apply cooldown to commands. */
public class CooldownMiddleware implements BukkitMiddleware {

  @Override
  public @NonNull Optional<BukkitResult> next(@NonNull CommandContext context) {
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
