package com.github.chevyself.starbox.experimental;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

public interface Adapter<C extends StarboxCommandContext, T extends StarboxCommand<C, T>> {

  void onRegister(@NonNull T command);

  void onUnregister(T command);

  void close();

  @NonNull
  CommandManager<C, T> initialize();

  @NonNull
  CommandManager<C, T> getCommandManager();
}
