package com.starfishst.bukkit.utils;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/** Utilities for files while using bukkit */
public class FilesUtils {

  /**
   * Get the file or the resource
   *
   * @param plugin the plugin looking for the file
   * @param fileName the name of the file
   * @return the file
   */
  @NotNull
  public static File getFileOrResource(@NotNull Plugin plugin, @NotNull String fileName) {
    File file = new File(plugin.getDataFolder(), fileName);
    if (!file.exists()) {
      plugin.saveResource(fileName, true);
    }
    return file;
  }
}
