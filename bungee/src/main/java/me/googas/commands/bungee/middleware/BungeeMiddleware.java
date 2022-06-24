package me.googas.commands.bungee.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.Result;

/** Middleware implementation for the 'Bungee' module. */
public interface BungeeMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<Result> next(@NonNull CommandContext context);
}
