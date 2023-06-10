package com.github.chevyself.starbox.experimental;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

public interface CommandParserFactory<
    C extends StarboxCommandContext, T extends StarboxCommand<C, T>> {

  @NonNull
  CommandParser<C, T> create(@NonNull CommandManager<C, T> commandManager);
}
