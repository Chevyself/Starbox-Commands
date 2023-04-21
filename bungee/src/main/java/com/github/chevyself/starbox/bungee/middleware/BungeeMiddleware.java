package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import java.util.Optional;
import lombok.NonNull;

/** Middleware implementation for the 'Bungee' module. */
public interface BungeeMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<BungeeResult> next(@NonNull CommandContext context);
}
