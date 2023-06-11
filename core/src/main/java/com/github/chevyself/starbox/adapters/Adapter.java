package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
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

  void registerDefaultProviders(@NonNull ProvidersRegistry<C> builder);

  void registerDefaultMiddlewares(@NonNull MiddlewareRegistry<C> middlewares);
}
