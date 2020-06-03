package com.starfishst.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** File utility for bungee */
public class FileUtils {

  /**
   * Get a file or resource
   *
   * @param plugin the plugin asking for the file
   * @param fileName the name of the file
   * @return the file
   * @throws IOException if the data folder could not be created or the resource could not be copied
   */
  @NotNull
  public static File getFileOrResource(@NotNull Plugin plugin, @NotNull String fileName)
      throws IOException {
    if (!plugin.getDataFolder().exists()) {
      if (!plugin.getDataFolder().mkdir()) {
        throw new IOException("Data folder coulld not be created");
      }
    }
    File file = new File(plugin.getDataFolder(), fileName);
    if (!file.exists()) {
      InputStream stream = plugin.getResourceAsStream(fileName);
      Files.copy(stream, file.toPath());
    }
    return file;
  }
}
