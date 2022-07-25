package me.googas.commands.bungee.middleware;

import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.BungeeResult;
import me.googas.commands.result.StarboxResult;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * This middleware sends the components of the {@link me.googas.commands.bungee.result.BungeeResult}
 * to the {@link net.md_5.bungee.api.CommandSender}.
 */
public class ResultHandlingMiddleware implements Middleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, StarboxResult result) {
    if (result instanceof BungeeResult && !((BungeeResult) result).getComponents().isEmpty()) {
      context
          .getSender()
          .sendMessage(((BungeeResult) result).getComponents().toArray(new BaseComponent[0]));
    }
  }
}
