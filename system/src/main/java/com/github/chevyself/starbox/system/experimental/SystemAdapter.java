package com.github.chevyself.starbox.system.experimental;

import com.github.chevyself.starbox.experimental.AbstractAdapter;
import com.github.chevyself.starbox.experimental.CommandManager;
import com.github.chevyself.starbox.system.SystemMessagesProvider;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemAdapter extends AbstractAdapter<CommandContext, SystemCommand> {

  private CommandListener listener;

  @Override
  public void onRegister(@NonNull SystemCommand command) {}

  @Override
  public void onUnregister(SystemCommand command) {}

  @Override
  public void close() {}

  @Override
  public @NonNull CommandManager<CommandContext, SystemCommand> initialize() {
    if (this.commandManager != null) {
      throw new IllegalStateException("Command manager already initialized");
    }
    this.commandManager =
        new CommandManager<>(this, SystemCommandParser::new, new SystemMessagesProvider());
    this.listener = new CommandListener(commandManager, ".");
    this.listener.start();
    return this.commandManager;
  }
}
