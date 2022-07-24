package me.googas.commands.bukkit.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.bukkit.result.Result;

/** Permission middleware. This checks the permission of the sender before executing a command */
public class PermissionMiddleware implements BukkitMiddleware {

  @Override
  public @NonNull Optional<BukkitResult> next(@NonNull CommandContext context) {
    final String permission = context.getCommand().getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!context.getSender().hasPermission(permission)) {
        return Optional.of(Result.of(context.getMessagesProvider().notAllowed(context)));
      }
    }
    return Optional.empty();
  }
}
