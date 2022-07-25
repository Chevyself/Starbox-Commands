package me.googas.commands.bungee.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.BungeeResult;
import me.googas.commands.bungee.result.Result;

/** Permission middleware. This checks the permission of the sender before executing a command */
public class PermissionMiddleware implements BungeeMiddleware {

  @Override
  public @NonNull Optional<BungeeResult> next(@NonNull CommandContext context) {
    final String permission = context.getCommand().getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!context.getSender().hasPermission(permission)) {
        return Optional.of(Result.of(context.getMessagesProvider().notAllowed(context)));
      }
    }
    return Optional.empty();
  }
}
