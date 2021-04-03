package me.googas.commands.bungee.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.utils.BungeeUtils;
import me.googas.commands.result.EasyResult;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * This is the implementation for {@link EasyResult} to be used in the execution of {@link
 * me.googas.commands.bungee.BungeeCommand}. This includes a {@link List} of {@link BaseComponent}
 * which will be send to the {@link net.md_5.bungee.api.CommandSender} upon the command execution.
 *
 * <p>If the {@link Result} from {@link
 * me.googas.commands.bungee.BungeeCommand#execute(CommandContext)} is null or empty no message will
 * be shown to the sender.
 *
 * <p>Exceptions will show a simple {@link Result} message and the stack trace will be printed.
 *
 * <p>To parse {@link BaseComponent} from a String use {@link BungeeUtils#getComponent(String)} to
 * know how to create a {@link BaseComponent} check <a
 * href="https://minecraft.tools/en/tellraw.php">minecraft-tools</a> or the <a
 * href="https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/overview-summary.html">bungee-api-chat</a>.
 */
public class Result implements EasyResult {

  /** The components that will be send after the execution */
  @NonNull @Getter private final List<BaseComponent> components = new ArrayList<>();

  /**
   * Create the result with a single component
   *
   * @param component the component to send as result
   */
  public Result(@NonNull BaseComponent component) {
    this.components.add(component);
  }

  /**
   * Create the result with many components. Useful to use when {@link
   * BungeeUtils#getComponent(String)} is used
   *
   * @param components the components to send as result
   */
  public Result(@NonNull BaseComponent... components) {
    this.components.addAll(Arrays.asList(components));
  }

  /**
   * Create a result with text. A {@link TextComponent} will be created and the text will be
   * formatted using {@link BungeeUtils#format(String)}
   *
   * @param text the text to send
   */
  public Result(@NonNull String text) {
    this(new TextComponent(BungeeUtils.format(text)));
  }

  /**
   * Create the result with a text. A {@link TextComponent} will be created and the text will be
   * formatted using {@link BungeeUtils#format(String, Map)}
   *
   * @param text the text to format and send
   * @param map the placeholders
   */
  public Result(@NonNull String text, @NonNull Map<String, String> map) {
    this(BungeeUtils.format(text, map));
  }

  /**
   * Create the result with a text. A {@link TextComponent} wil be created and the text will be
   * formatted using {@link BungeeUtils#format(String, Object...)}
   *
   * @param text the text to format and send
   * @param objects the placeholders
   */
  public Result(@NonNull String text, Object... objects) {
    this(BungeeUtils.format(text, objects));
  }

  @Override
  public String getMessage() {
    return ComponentSerializer.toString(this.components);
  }
}
