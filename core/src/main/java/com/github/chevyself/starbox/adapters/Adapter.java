package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

public interface Adapter<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  void onRegister(@NonNull T command);

  void onUnregister(T command);

  void close();

  @NonNull
  CommandManager<C, T> initialize();

  @NonNull
  CommandManager<C, T> getCommandManager();
}
