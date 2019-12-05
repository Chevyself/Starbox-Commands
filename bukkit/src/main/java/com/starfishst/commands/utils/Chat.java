package com.starfishst.commands.utils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Chat utils */
public class Chat {

  /**
   * Send a message to a sender using a message with place holder
   *
   * @param sender the sender to send the message
   * @param string the message
   * @param strings the strings to replace the place holders
   */
  public static void send(
      @Nullable CommandSender sender, @NotNull String string, @NotNull Object... strings) {
    if (sender != null) {
      sender.sendMessage(BukkitUtils.getMessage(string, strings));
    }
  }
}
