package com.github.chevyself.starbox.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
  @NonNull
  public <T> T get(@NonNull String key) {
    return Objects.requireNonNull((T) map.get(key), "Could not find object with key " + key);
  }
}
