package chevyself.github.commands.bukkit.middleware;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.result.BukkitResult;
import java.util.Optional;
import lombok.NonNull;

/** Middleware implementation for the 'Bukkit' module. */
public interface BukkitMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<BukkitResult> next(@NonNull CommandContext context);
}
