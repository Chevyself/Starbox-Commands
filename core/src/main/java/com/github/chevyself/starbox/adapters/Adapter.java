package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.NonNull;

public interface Adapter<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  void onRegister(@NonNull T command);

  void onUnregister(@NonNull T command);

  void close();

  void registerDefaultProviders(
      @NonNull CommandManagerBuilder<C, T> builder, @NonNull ProvidersRegistry<C> registry);

  void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<C, T> builder, @NonNull MiddlewareRegistry<C> middlewares);

  void onBuilt(@NonNull CommandManager<C, T> built);

  @NonNull
  CommandParser<C, T> createParser(@NonNull CommandManager<C, T> commandManager);

  @NonNull
  CommandMetadataParser getDefaultCommandMetadataParser();
}
