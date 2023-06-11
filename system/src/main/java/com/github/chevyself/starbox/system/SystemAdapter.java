package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.middleware.SystemResultHandlingMiddleware;
import com.github.chevyself.starbox.system.providers.SystemCommandContextProvider;
import lombok.NonNull;

public class SystemAdapter implements Adapter<CommandContext, SystemCommand> {

  @NonNull private final String prefix;
  private CommandListener listener;

  public SystemAdapter(@NonNull String prefix) {
    this.prefix = prefix;
  }

  @Override
  public void onRegister(@NonNull SystemCommand command) {}

  @Override
  public void onUnregister(@NonNull SystemCommand command) {}

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, SystemCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    registry.addProvider(new SystemCommandContextProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, SystemCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {
    middlewares.addGlobalMiddleware(new SystemResultHandlingMiddleware());
    middlewares.addGlobalMiddleware(new CooldownMiddleware<>(builder.getMessagesProvider()));
  }

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, SystemCommand> built) {
    listener = new CommandListener(built, prefix);
    listener.start();
  }

  @Override
  public @NonNull SystemCommandParser createParser(
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    return new SystemCommandParser(this, commandManager);
  }
}
