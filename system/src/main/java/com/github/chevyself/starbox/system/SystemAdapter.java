package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.adapters.AbstractAdapter;
import com.github.chevyself.starbox.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.middleware.SystemResultHandlingMiddleware;
import com.github.chevyself.starbox.system.providers.CommandContextProvider;
import lombok.NonNull;

public class SystemAdapter extends AbstractAdapter<CommandContext, SystemCommand> {

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
  public void close() {
    this.commandManager.close();
  }

  @Override
  protected void registerDefaultProviders() {
    super.registerDefaultProviders();
    this.providersRegistry.addProvider(new CommandContextProvider());
  }

  @Override
  protected void registerDefaultMiddlewares() {
    super.registerDefaultMiddlewares();
    middlewareRegistry.addGlobalMiddleware(new CooldownMiddleware<>(messagesProvider));
    middlewareRegistry.addGlobalMiddleware(new SystemResultHandlingMiddleware());
  }

  @Override
  public @NonNull CommandManager<CommandContext, SystemCommand> initialize() {
    super.initialize();
    listener = new CommandListener(commandManager, prefix);
    listener.start();
    return commandManager;
  }

  @Override
  protected @NonNull CommandManager<CommandContext, SystemCommand> createCommandManager() {
    return new CommandManager<>(
        this, SystemCommandParser::new, providersRegistry, middlewareRegistry, messagesProvider);
  }
}
