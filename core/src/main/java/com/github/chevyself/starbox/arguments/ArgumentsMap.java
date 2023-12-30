package com.github.chevyself.starbox.arguments;

import java.util.List;
import java.util.Map;
import lombok.NonNull;

public class ArgumentsMap {

  @NonNull private final Map<String, Object> arguments;
  @NonNull private final List<Object> extra;

  public ArgumentsMap(@NonNull Map<String, Object> arguments, @NonNull List<Object> extra) {
    this.arguments = arguments;
    this.extra = extra;
  }

  public Object get(@NonNull String name) {
    if (!this.arguments.containsKey(name)) {
      throw new IllegalArgumentException("No argument with name " + name + " found");
    }
    return this.arguments.get(name);
  }

  @NonNull
  public <T> T get(@NonNull String name, @NonNull Class<T> clazz) {
    Object object = this.get(name);
    if (!clazz.isInstance(object)) {
      throw new IllegalArgumentException(
          "Argument " + name + " is not of type " + clazz.getSimpleName());
    }
    return clazz.cast(object);
  }

  public <T> T get(@NonNull Class<T> clazz) {
    for (Object object : this.extra) {
      if (clazz.isInstance(object)) {
        return clazz.cast(object);
      }
    }
    throw new IllegalArgumentException("No argument of type " + clazz.getSimpleName() + " found");
  }
}
