package me.googas.commands.system.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.system.Result;
import me.googas.commands.system.context.CommandContext;

/** Implementation of middleware for the 'System' module. */
public interface SystemMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<Result> next(@NonNull CommandContext context);
}
