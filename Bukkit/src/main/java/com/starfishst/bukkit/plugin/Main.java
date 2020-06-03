package com.starfishst.bukkit.plugin;

import com.starfishst.bukkit.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;

/** The class to initialize as a bukkit plugin */
public class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    super.onEnable();
    Console.setLogger(this.getLogger());
  }
}
