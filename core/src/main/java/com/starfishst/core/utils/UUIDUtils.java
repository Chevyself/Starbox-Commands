package com.starfishst.core.utils;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/** Utils for {@link java.util.UUID} */
public class UUIDUtils {

  /**
   * Trims a uuid
   *
   * @param uuid to be trimmed
   * @return the trimmed uuid
   */
  @NotNull
  public static String trim(@NotNull UUID uuid) {
    return uuid.toString().replace("-", "");
  }

  /**
   * Converts a trimmed uuid to a normal uuid
   *
   * @param trimmed the uuid to convert
   * @return the uuid
   */
  public static UUID untrim(@NotNull String trimmed) {
    StringBuilder builder = Strings.getBuilder();
    builder.insert(20, "-");
    builder.insert(16, "-");
    builder.insert(12, "-");
    builder.insert(8, "-");
    return UUID.fromString(builder.toString());
  }
}
