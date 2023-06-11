package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.commands.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import lombok.NonNull;

public interface Adapter<C extends StarboxCommandContext<C, T>, T extends Command<C, T>> {

  void onRegister(@NonNull T command);

  void onUnregister(@NonNull T command);

  void close();

  Adapter<C, T> setDefaultMiddlewares();

  @NonNull
  CommandManager<C, T> initialize(@NonNull ProvidersRegistry<C> providersRegistry, @NonNull
  MessagesProvider<C> messagesProvider);

  @NonNull
  CommandManager<C, T> getCommandManager();
}
