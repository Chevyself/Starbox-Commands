package com.starfishst.bungee.utils;

import java.util.HashMap;
import me.googas.commons.JsonUtils;
import me.googas.commons.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeeUtils {

  /**
   * Build a message and give it colors
   *
   * @param string the message to build
   * @param placeholders the placeholders of the message. See {@link Strings#buildMessage(String,
   *     HashMap)}
   * @return the built message
   */
  @NotNull
  public static String build(
      @Nullable String string, @NotNull HashMap<String, String> placeholders) {
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
   * Parse the json into a component
   *
   * @param string the string to get the component from
   * @return the component
   */
  @NotNull
  public static BaseComponent[] getComponent(@NotNull String string) {
    if (JsonUtils.isJson(string)) {
      ComponentSerializer.parse(string);
    }
    return new BaseComponent[] {new TextComponent(string)};
  }
}
