package me.googas;

import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.CommandManager;
import me.googas.commands.system.SystemMessagesProvider;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.system.middleware.CooldownMiddleware;
import me.googas.commands.system.providers.CommandContextProvider;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new ProvidersRegistry<>(messagesProvider).addProvider(new CommandContextProvider());
    CommandManager manager = new CommandManager("-", registry, messagesProvider);
    manager.getGlobalMiddlewares().add(new CooldownMiddleware());
    manager.parseAndRegister(new Commands());
    while (true) {}
  }
}
