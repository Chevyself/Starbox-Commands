package me.googas.commands.bukkit.middleware;

import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.result.StarboxResult;

/**
 * This middleware sends the components of the {@link me.googas.commands.bukkit.result.BukkitResult}
 * to the {@link org.bukkit.command.CommandSender}.
 */
public class ResultHandlingMiddleware implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof BukkitResult) {
      BukkitUtils.send(context.getSender(), ((BukkitResult) result).getComponents());
    }
  }
}
