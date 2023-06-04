package me.googas;

import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.CommandManager;
import com.github.chevyself.starbox.system.SystemMessagesProvider;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.providers.CommandContextProvider;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    SystemMessagesProvider messagesProvider = new SystemMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new ProvidersRegistry<>(messagesProvider).addProvider(new CommandContextProvider());
    CommandManager manager =
        new CommandManager("-", registry, messagesProvider).addDefaultMiddlewares();
    // manager.registerAllIn("me.googas");
    manager.parseAndRegisterAll(new Commands());
    while (true) {}
  }
}
