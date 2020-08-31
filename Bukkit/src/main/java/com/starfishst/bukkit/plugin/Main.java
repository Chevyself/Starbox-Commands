package com.starfishst.bukkit.plugin;

import com.starfishst.bukkit.CommandManager;
import com.starfishst.bukkit.CommandManagerOptions;
import com.starfishst.bukkit.messages.DefaultMessagesProvider;
import com.starfishst.bukkit.plugin.commands.TestCommands;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import org.bukkit.plugin.java.JavaPlugin;

/** The class to initialize as a bukkit plugin */
public class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    CommandManager manager =
        new CommandManager(
            this,
            new CommandManagerOptions(false),
            new DefaultMessagesProvider(),
            new ProvidersRegistry<>());
    manager.registerCommand(new TestCommands());
    manager.registerPlugin();
  }
}
