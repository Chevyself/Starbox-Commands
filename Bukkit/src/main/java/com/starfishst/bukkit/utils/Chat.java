package com.starfishst.bukkit.utils;

import com.starfishst.utils.gson.JsonUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
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
      String message = BukkitUtils.getMessage(string, strings);
      if (JsonUtils.isJson(message)) {
        BaseComponent[] baseComponents = ComponentSerializer.parse(message);
        message = new TextComponent(baseComponents).toString();
      }
      sender.sendMessage(message);
    }
  }
}
