package com.starfishst.bungee.utils;

import com.starfishst.core.utils.Strings;
import com.starfishst.utils.gson.JsonUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Chat utilities for bungee */
public class Chat {

  /**
   * Colors a message
   *
   * @param message the message with placeholders
   * @param strings the placeholders
   * @return the built message and colored
   */
  @NotNull
  public static String color(@Nullable String message, Object... strings) {
    return ChatColor.translateAlternateColorCodes('&', Strings.buildMessage(message, strings));
  }

  /**
   * Sends the message
   *
   * @param sender to send the message
   * @param string the message with placeholders
   * @param strings the placeholders
   */
  public static void send(
      @NotNull CommandSender sender, @Nullable String string, Object... strings) {
    String message = color(string, strings);
    TextComponent toSend = new TextComponent(message);
    if (string != null && JsonUtils.isJson(string)) {
      BaseComponent[] baseComponents = ComponentSerializer.parse(message);
      toSend = new TextComponent(baseComponents);
    }
    sender.sendMessage(toSend);
  }
}
