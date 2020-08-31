package com.starfishst.core.utils;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/** Static utilities for {@link UUID} */
public class UUIDUtils {

  /**
   * Trims a uuid. This replaces every "-" character with nothing
   *
   * <p>Example:
   *
   * <p>UUID: cbb5b3ac-81d5-448b-895c-06b9ce39b904 Trimmed UUID: cbb5b3ac81d5448b895c06b9ce39b904
   *
   * @param uuid the UUID to be trimmed
   * @return the trimmed uuid
   */
  @NotNull
  public static String trim(@NotNull UUID uuid) {
    return uuid.toString().replace("-", "");
  }

  /**
   * Converts a trimmed uuid to a normal uuid. Check {@link #trim(UUID)}
   *
   * @param trimmed the trimmed uuid to get as a normal UUID
   * @return the uuid
   * @throws IllegalArgumentException if the trimmed string is not an actual UUID see {@link
   *     UUID#fromString(String)}
   */
  @NotNull
  public static UUID untrim(@NotNull String trimmed) {
    StringBuilder builder = Strings.getBuilder();
    builder.append(trimmed);
    builder.insert(20, "-");
    builder.insert(16, "-");
    builder.insert(12, "-");
    builder.insert(8, "-");
    return UUID.fromString(builder.toString());
  }
}
