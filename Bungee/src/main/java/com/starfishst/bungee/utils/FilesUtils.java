package com.starfishst.bungee.utils;

import java.io.File;
import java.io.IOException;
import me.googas.commons.CoreFiles;
import me.googas.commons.Validate;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** File utility for bungee */
public class FilesUtils {

  /**
   * Get the file or the resource
   *
   * @param plugin the plugin looking for the file
   * @param parent the directory that holds the file
   * @param fileName the name of the file
   * @return the file
   * @throws IOException if the parent directory of the file could not be created
   */
  @NotNull
  public static File getFileOrResource(
      @NotNull Plugin plugin, @Nullable String parent, @NotNull String fileName)
      throws IOException {
    return CoreFiles.getFileOrResource(
        parent,
        fileName,
        Validate.notNull(
            plugin.getResourceAsStream(fileName),
            "The resource " + fileName + " could not be found!"));
  }

  /**
   * Get a file or a resource without specifying a parent file
   *
   * @param plugin the plugin trying to get the file
   * @param fileName the name of the file
   * @return the file if found else null
   * @throws IOException if the parent directory (if it exists) could not be created
   */
  @NotNull
  public static File getFileOrResource(@NotNull Plugin plugin, @NotNull String fileName)
      throws IOException {
    return getFileOrResource(plugin, plugin.getDataFolder().getPath(), fileName);
  }
}
