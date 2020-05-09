package com.starfishst.bungee.utils;

import com.starfishst.core.utils.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Chat {

  @NotNull
  public static String color(@Nullable String message, Object... strings) {
    return ChatColor.translateAlternateColorCodes('&', Strings.buildMessage(message, strings));
  }

  public static void send(
      @NotNull CommandSender sender, @Nullable String message, Object... strings) {
    sender.sendMessage(new TextComponent(Chat.color(message, strings)));
  }
}
