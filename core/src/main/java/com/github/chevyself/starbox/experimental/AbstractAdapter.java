package com.github.chevyself.starbox.experimental;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.util.Objects;
import lombok.NonNull;

public abstract class AbstractAdapter<
        C extends StarboxCommandContext, T extends StarboxCommand<C, T>>
    implements Adapter<C, T> {

  public CommandManager<C, T> commandManager;

  @Override
  public @NonNull CommandManager<C, T> getCommandManager() {
    return Objects.requireNonNull(commandManager, "CommandManager has not been initialized yet.");
  }
}
