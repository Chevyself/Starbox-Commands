package chevyself.github.commands.bukkit.middleware;

import chevyself.github.commands.bukkit.CooldownManager;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.result.BukkitResult;
import chevyself.github.commands.bukkit.result.Result;
import chevyself.github.commands.result.StarboxResult;
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
