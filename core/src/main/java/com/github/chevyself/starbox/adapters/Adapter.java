package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.NonNull;

public interface Adapter<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  void onRegister(@NonNull T command);

  void onUnregister(@NonNull T command);

  void close();

  @NonNull
  Adapter<C, T> setProvidersRegistry(@NonNull ProvidersRegistry<C> providersRegistry);

  @NonNull
  Adapter<C, T> setMiddlewareRegistry(@NonNull MiddlewareRegistry<C> middlewareRegistry);

  @NonNull
  Adapter<C, T> setMessagesProvider(@NonNull MessagesProvider<C> messagesProvider);

  @NonNull
  Adapter<C, T> useDefaultMiddlewares(boolean use);

  @NonNull
  Adapter<C, T> useDefaultProviders(boolean use);

  @NonNull
  CommandManager<C, T> initialize();

  @NonNull
  CommandManager<C, T> getCommandManager();
}
