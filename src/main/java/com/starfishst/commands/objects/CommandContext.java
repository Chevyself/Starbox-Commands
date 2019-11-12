package com.starfishst.commands.objects;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandContext {

  @NotNull
  private final CommandSender sender;
  @NotNull
  private final String command;
  @NotNull
  private final String[] strings;

  public CommandContext(
      @NotNull CommandSender sender, @NotNull String command, @NotNull String[] strings) {
    this.sender = sender;
    this.command = command;
    this.strings = strings;
  }

  @NotNull
  public CommandSender getSender() {
    return sender;
  }

  @NotNull
  public Player getPlayer() {
    return (Player) sender;
  }

  @NotNull
  @SuppressWarnings("unused")
  public String getCommand() {
    return command;
  }

  @SuppressWarnings("unused")
  public String[] getStrings() {
    return strings;
  }
}
