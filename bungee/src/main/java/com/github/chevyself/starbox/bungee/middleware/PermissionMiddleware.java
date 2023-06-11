package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.messages.BungeeMessagesProvider;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.Optional;
import lombok.NonNull;

/** Permission middleware. This checks the permission of the sender before executing a command */
public class PermissionMiddleware implements BungeeMiddleware {

  @NonNull private final BungeeMessagesProvider messagesProvider;

  public PermissionMiddleware(@NonNull BungeeMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Optional<StarboxResult> next(@NonNull CommandContext context) {
    final String permission = context.getCommand().getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!context.getSender().hasPermission(permission)) {
        return Optional.of(BungeeResult.of(messagesProvider.notAllowed(context)));
      }
    }
    return Optional.empty();
  }
}
