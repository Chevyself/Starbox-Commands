package com.starfishst.bukkit.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import me.googas.commons.Strings;
import me.googas.commons.cache.Cache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Utils for bukkit */
public class BukkitUtils {

  /**
   * Adds colors to a string
   *
   * @param string the string
   * @return the colored string
   */
  @Deprecated
  @NotNull
  public static String color(@Nullable String string) {
    return ChatColor.translateAlternateColorCodes('&', string == null ? "$cNull" : string);
  }

  /**
   * Build a message and give it colors
   *
   * @param string the message to build
   * @param placeholders the placeholders of the message. See {@link Strings#buildMessage(String,
   *     Map)}
   * @return the built message
   */
  @NotNull
  public static String build(@Nullable String string, @NotNull Map<String, String> placeholders) {
    return ChatColor.translateAlternateColorCodes('&', Strings.buildMessage(string, placeholders));
  }

  /**
   * Build a message and give it colors
   *
   * @param string the message to build
   * @return the built message
   */
  @NotNull
  public static String build(@Nullable String string) {
    return ChatColor.translateAlternateColorCodes('&', Strings.buildMessage(string));
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

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   * @param objects to change the placeholders in the command
   */
  public static void dispatch(
      @NotNull CommandSender sender, @NotNull String command, Object... objects) {
    dispatch(sender, Strings.buildMessage(command, objects));
  }

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   * @param placeholders the placeholders to change in the command line
   */
  public static void dispatch(
      @NotNull CommandSender sender,
      @NotNull String command,
      @NotNull HashMap<String, String> placeholders) {
    dispatch(sender, Strings.buildMessage(command, placeholders));
  }

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   */
  public static void dispatch(@NotNull CommandSender sender, @NotNull String command) {
    Bukkit.dispatchCommand(sender, command);
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to send
   * @param objects to change the placeholders in the command
   */
  public static void dispatch(@NotNull String command, Object... objects) {
    dispatch(Strings.buildMessage(command, objects));
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to send
   * @param placeholders the placeholders to change in the command line
   */
  public static void dispatch(@NotNull String command, HashMap<String, String> placeholders) {
    dispatch(Strings.buildMessage(command, placeholders));
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to dispatch
   */
  public static void dispatch(@NotNull String command) {
    dispatch(Bukkit.getConsoleSender(), command);
  }

  /**
   * Get bukkit command map
   *
   * @return bukkit command map
   * @throws IllegalAccessException in case that the field commandMap in {@link Server} cannot be
   *     accessed
   * @throws NoSuchFieldException in case the field commandMap in {@link Server} cannot be accessed
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
   * Starts the cache task for bukkit
   *
   * @param plugin the plugin that will start the cache
   */
  public static void startCache(@NotNull Plugin plugin) {
    Cache.cancelTask();
    Bukkit.getServer().getScheduler().runTaskTimer(plugin, Cache.getTask(), 0, 20L);
  }
}
