package me.googas.commands.jda.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.cooldown.CooldownManager;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.result.StarboxResult;

/** Middleware to check and apply cooldown to commands. */
public class CooldownMiddleware implements JdaMiddleware {

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    Optional<CooldownManager> optional = context.getCommand().getCooldownManager();
    return optional.map(
        cooldown -> {
          if (cooldown.hasCooldown(context)) {
            return Result.forType(ResultType.ERROR)
                .setDescription(
                    context.getMessagesProvider().cooldown(context, cooldown.getTimeLeft(context)))
                .build();
          }
          return null;
        });
  }

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    Optional<CooldownManager> optional = context.getCommand().getCooldownManager();
    if (result.isCooldown() && optional.isPresent()) {
      optional.get().refresh(context);
    }
  }
}
