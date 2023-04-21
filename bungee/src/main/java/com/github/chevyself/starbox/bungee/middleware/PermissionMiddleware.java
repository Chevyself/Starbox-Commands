package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.bungee.result.Result;
import java.util.Optional;
import lombok.NonNull;

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
