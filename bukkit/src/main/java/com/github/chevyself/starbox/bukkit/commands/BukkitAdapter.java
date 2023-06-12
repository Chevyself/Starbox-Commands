package com.github.chevyself.starbox.bukkit.commands;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class BukkitAdapter {

  @NonNull @Getter private final Plugin plugin;

  public BukkitAdapter(@NonNull Plugin plugin) {
    this.plugin = plugin;
  }
}
