package chevyself.github.commands.bukkit.middleware;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.result.BukkitResult;
import chevyself.github.commands.bukkit.utils.BukkitUtils;
import chevyself.github.commands.result.StarboxResult;
import lombok.NonNull;

/**
 * This middleware sends the components of the {@link BukkitResult} to the {@link
 * org.bukkit.command.CommandSender}.
 */
public class ResultHandlingMiddleware implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof BukkitResult) {
      BukkitUtils.send(context.getSender(), ((BukkitResult) result).getComponents());
    }
  }
}
