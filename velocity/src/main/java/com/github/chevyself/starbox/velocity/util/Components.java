package com.github.chevyself.starbox.velocity.util;

import com.github.chevyself.starbox.common.JsonUtils;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.velocity.result.ComponentResult;
import java.util.List;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Components {

  @NonNull
  public static String toString(List<Component> components) {
    StringBuilder builder = new StringBuilder();
    for (Component component : components) {
      builder.append(LegacyComponentSerializer.legacyAmpersand().serialize(component));
    }
    return builder.toString();
  }

  @NonNull
  public static Result asResult(@NonNull String plain) {
    Component component;
    if (JsonUtils.isJson(plain)) {
      component = GsonComponentSerializer.gson().deserialize(plain);
    } else {
      component = LegacyComponentSerializer.legacy('&').deserialize(plain);
    }
    return Components.asResult(component);
  }

  @NonNull
  public static Result asResult(@NonNull Component component) {
    return new ComponentResult(component);
  }
}
