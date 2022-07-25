package me.googas.commands.bungee.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.utils.BungeeUtils;
import me.googas.commands.bungee.utils.Components;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * This is the implementation for {@link BungeeResult}.
 *
 * <p>This includes a {@link List} of {@link BaseComponent} which will be sent to the {@link
 * net.md_5.bungee.api.CommandSender} upon the command execution.
 *
 * <p>If the {@link BungeeResult} from {@link
 * me.googas.commands.bungee.BungeeCommand#execute(CommandContext)} is null or empty no message will
 * be shown to the sender.
 *
 * <p>To parse {@link BaseComponent} from a String use {@link Components#getComponent(String)} to
 * know how to create a {@link BaseComponent} check <a
 * href="https://minecraft.tools/en/tellraw.php">minecraft-tools</a> or the <a
 * href="https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/overview-summary.html">bungee-api-chat</a>.
 */
public class Result implements BungeeResult {

  /** The components that will be sent after the execution. */
  @NonNull @Getter private final List<BaseComponent> components = new ArrayList<>();

  /** Create an empty result. */
  public Result() {}

  /**
   * Create the result with a single component.
   *
   * @param component the component to send as result
   */
  public Result(@NonNull BaseComponent component) {
    this.components.add(component);
  }

  /**
   * Create the result with many components. Useful to use when {@link
   * Components#getComponent(String)} is used
   *
   * @param components the components to send as result
   */
  public Result(@NonNull BaseComponent... components) {
    this.components.addAll(Arrays.asList(components));
  }

  /**
   * Get a result from text. this will get the component using {@link
   * Components#getComponent(String)} while formatting the string {@link BungeeUtils#format(String)}
   *
   * @param text to get the component from
   * @return the result
   */
  public static Result of(@NonNull String text) {
    return new Result(Components.getComponent(text));
  }

  @Override
  public @NonNull Optional<String> getMessage() {
    return Optional.ofNullable(ComponentSerializer.toString(this.components));
  }
}
