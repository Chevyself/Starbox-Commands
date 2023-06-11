package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.CommandParser;
import lombok.NonNull;

public interface CommandParserFactory<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull
  CommandParser<C, T> create(@NonNull CommandManager<C, T> commandManager);
}
