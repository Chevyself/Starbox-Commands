package com.starfishst.commands.utils;

import com.starfishst.core.utils.Strings;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Utils for bukkit */
public class BukkitUtils {

  /**
   * Get bukkit command map
   *
   * @return bukkit command map
   */
  @NotNull
  public static CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
    final Server server = Bukkit.getServer();
    final Class<? extends Server> serverClass = server.getClass();
    final Field commandMapField = serverClass.getDeclaredField("commandMap");
    commandMapField.setAccessible(true);
    return (CommandMap) commandMapField.get(server);
  }

  /**
   * Adds colors to a string
   *
   * @param string the string
   * @return the colored string
   */
  @NotNull
  public static String color(@Nullable String string) {
    return ChatColor.translateAlternateColorCodes('&', string == null ? "$cNull" : string);
  }

  /**
   * Creates a message with colors and place holders
   *
   * @param message the message
   * @param strings the place holders
   * @return the built colored message
   */
  @NotNull
  public static String getMessage(@Nullable String message, Object... strings) {
    return BukkitUtils.color(Strings.buildMessage(message, strings));
  }
}
