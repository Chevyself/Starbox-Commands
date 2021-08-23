package me.googas.commands.bungee.utils;

import java.util.Map;
import lombok.NonNull;
import me.googas.starbox.Strings;
import net.md_5.bungee.api.ChatColor;

/**
 * Static utilities for Bungee.
 *
 * <p>Here you will find
 *
 * <ul>
 *   <li>String formatting
 * </ul>
 */
public class BungeeUtils {

  /**
   * Give colors to a message and format using {@link Strings#format(String)}.
   *
   * <p>Colors must be given with the color code char: '&amp;'
   *
   * <p>To know how to give colors to a message checkout <a
   * href="https://codepen.io/0biwan/full/ggVemP">this snippet</a>
   *
   * <p>A simple line to give colors should look like this:
   *
   * <ul>
   *   <li>'&amp;lHere's bold text'
   *   <li>'&amp;cThis is red text'
   *   <li>'&amp;5This is purple text'
   *   <li>'&amp;l&amp;6This is bold and gold text'
   * </ul>
   *
   * Here's each code of color:
   *
   * <ul>
   *   <li>'&amp;0' = Black
   *   <li>'&amp;1' = Dark blue
   *   <li>'&amp;2' = Dark green
   *   <li>'&amp;3' = Cyan
   *   <li>'&amp;4' = Dark red
   *   <li>'&amp;5' = Purple
   *   <li>'&amp;6' = Gold
   *   <li>'&amp;7' = Gray
   *   <li>'&amp;8' = Dark gray
   *   <li>'&amp;9' = Blue
   *   <li>'&amp;a' = Green
   *   <li>'&amp;b' = Aqua
   *   <li>'&amp;c' = Red
   *   <li>'&amp;d' = Magenta
   *   <li>'&amp;e' = Yellow
   *   <li>'&amp;f' = White
   *   <li>'&amp;l' = Bold
   *   <li>'&amp;n' = Underline
   *   <li>'&amp;o' = Italic
   *   <li>'&amp;m' = Strikethrough
   *   <li>'&amp;k' = Obfuscated
   *   <li>'&amp;r' = Resets the color
   * </ul>
   *
   * @param string the message to format
   * @return the formatted message
   */
  @NonNull
  public static String format(String string) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string));
  }

  /**
   * Give colors to a message and format using {@link Strings#format(String, Map)}.
   *
   * <p>To know how to use colors check {@link BungeeUtils#format(String)}
   *
   * @param string the message to format
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the formatted message
   */
  @NonNull
  public static String format(String string, @NonNull Map<String, String> placeholders) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string, placeholders));
  }

  /**
   * Gives colors to a message and format using {@link Strings#format(String, Object...)}
   *
   * <p>To know how to use colors check {@link BungeeUtils#format(String)}
   *
   * @param message the message to format
   * @param strings the objects that will replace the placeholders
   * @return the formatted message
   */
  @NonNull
  public static String format(String message, Object... strings) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(message, strings));
  }
}
