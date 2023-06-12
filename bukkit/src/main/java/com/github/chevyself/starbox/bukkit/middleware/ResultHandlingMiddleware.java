package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.result.Result;
import lombok.NonNull;

/**
 * This middleware sends the components of the {@link BukkitResult} to the {@link
 * org.bukkit.command.CommandSender}.
 */
public class ResultHandlingMiddleware implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    if (result instanceof BukkitResult) {
      BukkitUtils.send(context.getSender(), ((BukkitResult) result).getComponents());
    }
  }
}
