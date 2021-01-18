package com.starfishst.commands.bukkit.utils;

import java.io.File;
import java.io.IOException;
import lombok.NonNull;
import me.googas.commons.CoreFiles;
import me.googas.commons.Validate;
import org.bukkit.plugin.Plugin;

/**
 * Utilities for files while using bukkit
 *
 * @deprecated since PRE8 since its not very useful just use {@link CoreFiles}
 */
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
  @NonNull
  public static File getFileOrResource(
      @NonNull Plugin plugin, String parent, @NonNull String fileName) throws IOException {
    return CoreFiles.getFileOrResource(
        parent,
        fileName,
        Validate.notNull(
            plugin.getResource(fileName), "The resource " + fileName + " could not be found!"));
  }

  /**
   * Get a file or a resource without specifying a parent file
   *
   * @param plugin the plugin trying to get the file
   * @param fileName the name of the file
   * @return the file if found else null
   * @throws IOException if the parent directory (if it exists) could not be created
   */
  @NonNull
  public static File getFileOrResource(@NonNull Plugin plugin, @NonNull String fileName)
      throws IOException {
    return getFileOrResource(plugin, plugin.getDataFolder().getPath(), fileName);
  }
}
