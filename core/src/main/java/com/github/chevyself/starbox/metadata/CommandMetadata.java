package com.github.chevyself.starbox.metadata;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class CommandMetadata {

  @NonNull private final Map<String, Object> map = new HashMap<>();

  @NonNull
  public CommandMetadata put(@NonNull String key, @NonNull Object value) {
    map.put(key, value);
    return this;
  }

  @NonNull
  public CommandMetadata remove(@NonNull String key) {
    map.remove(key);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(@NonNull String key) {
    return (T) map.get(key);
  }
}
