package me.googas;

import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.CommandManager;
import com.github.chevyself.starbox.system.SystemMessagesProvider;
import com.github.chevyself.starbox.system.context.CommandContext;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    ProvidersRegistry<CommandContext> registry = new ProvidersRegistry<>(messagesProvider);
    CommandManager manager =
        new CommandManager("-", registry, messagesProvider).addDefaultMiddlewares();
    // manager.registerAllIn("me.googas");
    manager.registerAllIn("me.googas");
    registry.addProviders(
        manager.getParser().parseProviders("com.github.chevyself.starbox.system.providers"));
    while (true) {}
  }
}
