package me.googas;

import chevyself.github.commands.providers.registry.ProvidersRegistry;
import chevyself.github.commands.system.CommandManager;
import chevyself.github.commands.system.SystemMessagesProvider;
import chevyself.github.commands.system.context.CommandContext;
import chevyself.github.commands.system.providers.CommandContextProvider;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new ProvidersRegistry<>(messagesProvider).addProvider(new CommandContextProvider());
    CommandManager manager =
        new CommandManager("-", registry, messagesProvider)
            .addDefaultMiddlewares()
            .parseAndRegister(new Commands());
    while (true) {}
  }
}
