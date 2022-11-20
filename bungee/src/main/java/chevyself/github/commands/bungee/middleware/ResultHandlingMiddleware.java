package chevyself.github.commands.bungee.middleware;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.result.BungeeResult;
import chevyself.github.commands.result.StarboxResult;
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
