package me.googas.commands.bukkit.plugin;

import java.util.ArrayList;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.messages.DefaultMessagesProvider;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import org.bukkit.plugin.java.JavaPlugin;

/** TODO documentation */
public class EasyCommandsBukkit extends JavaPlugin {

  @Override
  public void onEnable() {
    // Create the instance of the manager
    CommandManager manager =
        new CommandManager(
            this, new BukkitProvidersRegistry(), new DefaultMessagesProvider(), new ArrayList<>());
    super.onEnable();
  }
}
