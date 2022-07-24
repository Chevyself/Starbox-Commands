package me.googas.commands.bukkit.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bukkit.utils.Components;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** This is the default implementation {@link BukkitResult} for the 'Bukkit' module. */
public class Result implements BukkitResult {

  @Getter @Setter @NonNull private List<BaseComponent> components = new ArrayList<>();

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
   * Components#getComponent(String)} while formatting the string {@link BukkitUtils#format(String)}
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
