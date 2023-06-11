package me.googas;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.SystemAdapter;
import com.github.chevyself.starbox.system.SystemMessagesProvider;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    SystemAdapter adapter = new SystemAdapter(".");
    CommandManager<CommandContext, SystemCommand> manager = adapter
        .initialize(new ProvidersRegistry<>(messagesProvider), messagesProvider);
    manager.parseAndRegister(new Commands());
  }
}
