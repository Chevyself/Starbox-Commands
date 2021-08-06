package me.googas;

import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.CommandManager;
import me.googas.commands.system.SystemMessagesProvider;
import me.googas.commands.system.context.CommandContext;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    ProvidersRegistry<CommandContext> registry = new ProvidersRegistry<>(messagesProvider);
    CommandManager manager = new CommandManager("-", registry, messagesProvider);
    manager.parseAndRegister(new Commands());
    while (true) {}
  }
}
