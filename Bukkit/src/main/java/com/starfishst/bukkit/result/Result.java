package com.starfishst.bukkit.result;

import com.starfishst.bukkit.utils.BukkitUtils;
import com.starfishst.core.result.IResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** The result that can be send by the execution of the command */
public class Result implements IResult {

  /** The components that will be send after the execution */
  @NonNull private final List<BaseComponent> components = new ArrayList<>();

  /**
   * Create the result with a component
   *
   * @param component the component to send as result
   */
  public Result(@NonNull BaseComponent component) {
    this.components.add(component);
  }

  /**
   * Create the result with many components
   *
   * @param components the components to send as result
   */
  public Result(@NonNull BaseComponent... components) {
    this.components.addAll(Arrays.asList(components));
  }

  /**
   * Create a result with a text
   *
   * @param text the text to send
   */
  public Result(@NonNull String text) {
    this(new TextComponent(BukkitUtils.build(text)));
  }

  /**
   * Create the result with a text
   *
   * @param text the text to send but has placeholders that will be changed using the map
   * @param map the map to change the placeholders
   */
  public Result(@NonNull String text, @NonNull HashMap<String, String> map) {
    this(BukkitUtils.build(text, map));
  }

  /**
   * Get the components in the result
   *
   * @return the components
   */
  @NonNull
  public List<BaseComponent> getComponents() {
    return components;
  }

  @Override
  public String getMessage() {
    return ComponentSerializer.toString(this.components);
  }
}
