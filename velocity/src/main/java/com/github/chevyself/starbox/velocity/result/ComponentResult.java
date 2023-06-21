package com.github.chevyself.starbox.velocity.result;

import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.velocity.util.Components;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

public class ComponentResult extends SimpleResult {
  /** The components that will be sent after the execution. */
  @NonNull @Getter private final List<Component> components;

  public ComponentResult(@NonNull List<Component> components) {
    super(Components.toString(components));
    this.components = components;
  }

  public ComponentResult(@NonNull Component... components) {
    this(Arrays.asList(components));
  }
}
