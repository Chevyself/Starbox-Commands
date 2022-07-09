package me.googas.commands.jda.middleware;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.Result;

/** This is an implementation of {@link Middleware} for the JDA module. */
public interface JdaMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<Result> next(@NonNull CommandContext context);
}
