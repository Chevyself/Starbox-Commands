package com.starfishst.commands.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Strings {

  @NotNull private static final StringBuilder builder = new StringBuilder();

  @NotNull
  public static StringBuilder getBuilder() {
    builder.setLength(0);
    return builder;
  }

  @NotNull
  public static String getMessage(@Nullable Object message, Object... strings) {
    return color(buildMessage(message, strings));
  }

  @NotNull
  public static String buildMessage(@Nullable Object message, Object... strings) {
    if (message != null) {
      for (int i = 0; i < strings.length; i++) {
        message =
            message
                .toString()
                .replace("{" + i + "}", strings[i] == null ? "&cNull" : strings[i].toString());
      }
    } else {
      message = "&cNull";
    }
    return message.toString();
  }

  @NotNull
  public static String color(@NotNull String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  @NotNull
  public static List<String> getPlayerNames() {
    List<String> players = new ArrayList<>();
    final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
    onlinePlayers.forEach(player -> players.add(player.getName()));
    return players;
  }

  @NotNull
  public static String getCorrectCommandString(@NotNull String commandString) {
    if (commandString.startsWith("//")) {
      commandString = commandString.substring(2);
    } else if (commandString.startsWith("/")) {
      commandString = commandString.substring(1);
    }
    return commandString;
  }

  @NotNull
  public static List<String> removeFirstAlias(@NotNull String[] oldArray) {
    if (oldArray.length >= 1) {
      return new ArrayList<>(Arrays.asList(oldArray).subList(1, oldArray.length));
    } else {
      return new ArrayList<>();
    }
  }

  public static String fromArray(@NotNull String[] strings) {
    StringBuilder builder = getBuilder();
    for (String string : strings) {
      builder.append(string).append(" ");
    }
    if (builder.length() >= 1) {
      builder.deleteCharAt(strings.length - 1);
    }
    return builder.toString();
  }

  @NotNull
  public static String[] arrayFrom(String[] oldArray, int position) {
    String[] newArray = new String[oldArray.length - position];
    if (oldArray.length - position >= 0) {
      System.arraycopy(oldArray, position, newArray, position, oldArray.length - position);
    }
    return newArray;
  }
}
