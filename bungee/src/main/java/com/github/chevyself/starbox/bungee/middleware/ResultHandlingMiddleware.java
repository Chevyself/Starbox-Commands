package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.result.StarboxResult;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * This middleware sends the components of the {@link BungeeResult} to the {@link
 * net.md_5.bungee.api.CommandSender}.
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
