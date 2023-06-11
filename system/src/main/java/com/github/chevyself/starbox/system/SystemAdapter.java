package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.adapters.AbstractAdapter;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.middleware.SystemResultHandlingMiddleware;
import com.github.chevyself.starbox.system.providers.CommandContextProvider;
import lombok.NonNull;


public class SystemAdapter extends AbstractAdapter<CommandContext, SystemCommand> {

  @NonNull
  private final String prefix;
  private CommandListener listener;

  public SystemAdapter(@NonNull String prefix) {
    this.prefix = prefix;
  }


  @Override
  public void onRegister(@NonNull SystemCommand command) {

  }

  @Override
  public void onUnregister(@NonNull SystemCommand command) {

  }

  @Override
  public void close() {
    this.commandManager.close();
  }

  @Override
  public @NonNull CommandManager<CommandContext, SystemCommand> initialize(
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider) {
    if (commandManager != null) {
      throw new IllegalStateException("CommandManager has already been initialized.");
    } else {
      providersRegistry.addProviders(new CommandContextProvider());
      this.commandManager = new CommandManager<>(this, SystemCommandParser::new, providersRegistry, messagesProvider);
      this.commandManager.addGlobalMiddleware(new CooldownMiddleware<>(messagesProvider));
      this.commandManager.addGlobalMiddleware(new SystemResultHandlingMiddleware());
      this.listener = new CommandListener(this.commandManager, this.prefix);
      this.listener.start();
      return commandManager;
    }
  }
}
