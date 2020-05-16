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


  /**
   * Get the millis from a local date
   *
   * @param date to get the millis from
   * @return the millis
   */
  public static long getMillisFromLocalDate(@NotNull LocalDateTime date){
    return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Get the time since a date
   *
   * @param date the date to get the time from
   * @return the time since a date
   */
  @NotNull
  public static Time getTimeFromToday(@NotNull LocalDateTime date) {
    long millis = (System.currentTimeMillis() - getMillisFromLocalDate(date));
    if (millis < 0) {
      millis *= -1;
    }
    return Time.fromMillis(millis);
  }
  
}
