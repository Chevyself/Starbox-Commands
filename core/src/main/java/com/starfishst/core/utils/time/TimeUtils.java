package com.starfishst.core.utils.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.jetbrains.annotations.NotNull;

/** Utilities for time */
public class TimeUtils {

  /**
   * Get local date from millis
   *
   * @param millis the millis to get the date from
   * @return the date
   */
  @NotNull
  public static LocalDateTime getLocalDateFromMillis(long millis) {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * Get the offset date from millis
   *
   * @param millis the millis to get the offset date
   * @return the offset date
   */
  @NotNull
  public static OffsetDateTime getOffsetDateFroMillis(long millis) {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }
}
