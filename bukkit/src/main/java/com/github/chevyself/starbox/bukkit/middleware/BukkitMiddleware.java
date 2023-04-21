package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import java.util.Optional;
import lombok.NonNull;

/** Middleware implementation for the 'Bukkit' module. */
public interface BukkitMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<BukkitResult> next(@NonNull CommandContext context);
}
