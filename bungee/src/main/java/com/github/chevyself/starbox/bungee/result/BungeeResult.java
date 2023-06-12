package com.github.chevyself.starbox.bungee.result;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.utils.BungeeUtils;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.result.type.SimpleResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * This is the implementation for {@link BungeeResult}.
 *
 * <p>This includes a {@link List} of {@link BaseComponent} which will be sent to the {@link
 * net.md_5.bungee.api.CommandSender} upon the command execution.
 *
 * <p>If the {@link BungeeResult} from {@link BungeeCommand#execute(CommandContext)} is null or
 * empty no message will be shown to the sender.
 *
 * <p>To parse {@link BaseComponent} from a String use {@link Components#getComponent(String)} to
 * know how to create a {@link BaseComponent} check <a
 * href="https://minecraft.tools/en/tellraw.php">minecraft-tools</a> or the <a
 * href="https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/overview-summary.html">bungee-api-chat</a>.
 */
public class BungeeResult extends SimpleResult {

  /** The components that will be sent after the execution. */
  @NonNull @Getter private final List<BaseComponent> components = new ArrayList<>();

  /** Create an empty result. */
  public BungeeResult(@NonNull List<BaseComponent> components) {
    super(ComponentSerializer.toString(components));
  }

  public BungeeResult() {
    this(new ArrayList<>());
  }

  /**
   * Create the result with a single component.
   *
   * @param component the component to send as result
   */
  public BungeeResult(@NonNull BaseComponent component) {
    this(Collections.singletonList(component));
  }

  /**
   * Create the result with many components. Useful to use when {@link
   * Components#getComponent(String)} is used
   *
   * @param components the components to send as result
   */
  public BungeeResult(@NonNull BaseComponent... components) {
    this(Arrays.asList(components));
  }

  /**
   * Get a result from text. this will get the component using {@link
   * Components#getComponent(String)} while formatting the string {@link BungeeUtils#format(String)}
   *
   * @param text to get the component from
   * @return the result
   */
  public static BungeeResult of(@NonNull String text) {
    return new BungeeResult(Components.getComponent(text));
  }
}
