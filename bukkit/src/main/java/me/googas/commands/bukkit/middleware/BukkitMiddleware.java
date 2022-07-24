package me.googas.commands.bukkit.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;

/** Middleware implementation for the 'Bukkit' module. */
public interface BukkitMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<BukkitResult> next(@NonNull CommandContext context);
}
