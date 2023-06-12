package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.result.type.SimpleResult;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ComponentResult extends SimpleResult {
  /** The components that will be sent after the execution. */
  @NonNull @Getter private final List<BaseComponent> components;

  public ComponentResult(@NonNull List<BaseComponent> components) {
    super(ComponentSerializer.toString(components));
    this.components = components;
  }

  public ComponentResult(@NonNull BaseComponent[] components) {
    this(Arrays.asList(components));
  }
}
