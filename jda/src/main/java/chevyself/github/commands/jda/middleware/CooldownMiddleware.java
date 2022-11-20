package chevyself.github.commands.jda.middleware;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.cooldown.CooldownManager;
import chevyself.github.commands.jda.result.Result;
import chevyself.github.commands.jda.result.ResultType;
import chevyself.github.commands.result.StarboxResult;
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
