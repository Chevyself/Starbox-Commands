package com.github.chevyself.starbox.jda.middleware;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.cooldown.CooldownManager;
import com.github.chevyself.starbox.jda.result.Result;
import com.github.chevyself.starbox.jda.result.ResultType;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.Optional;
import lombok.NonNull;

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
