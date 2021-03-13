package me.googas.commands.bungee.utils;

import java.util.HashMap;
import lombok.NonNull;
import me.googas.commons.JsonUtils;
import me.googas.commons.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeUtils {

  /**
   * Build a message and give it colors
   *
   * @param string the message to build
   * @param placeholders the placeholders of the message. See {@link Strings#build(String)} (String,
   *     HashMap)}
   * @return the built message
   */
  @NonNull
  public static String build(String string, @NonNull HashMap<String, String> placeholders) {
    return ChatColor.translateAlternateColorCodes('&', Strings.build(string, placeholders));
  }

  /**
   * Build a message and give it colors
   *
   * @param string the message to build
   * @return the built message
   */
  @NonNull
  public static String build(String string) {
    return ChatColor.translateAlternateColorCodes('&', Strings.build(string));
  }

  /**
   * Parse the json into a component
   *
   * @param string the string to get the component from
   * @return the component
   */
  @NonNull
  public static BaseComponent[] getComponent(@NonNull String string) {
    if (JsonUtils.isJson(string)) {
      return ComponentSerializer.parse(string);
    }
    return new BaseComponent[] {new TextComponent(string)};
  }
}
