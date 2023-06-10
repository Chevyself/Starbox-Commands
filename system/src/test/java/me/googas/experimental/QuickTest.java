package me.googas.experimental;

import com.github.chevyself.starbox.experimental.CommandManager;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.experimental.SystemAdapter;
import com.github.chevyself.starbox.system.experimental.SystemCommand;

public class QuickTest {

  public static void main(String[] args) {
    SystemAdapter adapter = new SystemAdapter();
    CommandManager<CommandContext, SystemCommand> commandManager = adapter.initialize();
    commandManager.parseAndRegister(new Commands());
    while (true) {}
  }
}
