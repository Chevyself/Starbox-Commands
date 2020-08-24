package com.starfishst.core.utils.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.jetbrains.annotations.NotNull;

/** Static utilities for time */
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
  public static OffsetDateTime getOffsetDateFromMillis(long millis) {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }

  /**
   * Get the millis from a local date
   *
   * @param date to get the millis from
   * @return the millis
   */
  public static long getMillisFromLocalDate(@NotNull LocalDateTime date) {
    return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
  /**
   * Get the millis from a local date
   *
   * @param date to get the millis from
   * @return the millis
   */
  public static long getMillisFromOffsetDate(@NotNull OffsetDateTime date) {
    return date.toInstant().toEpochMilli();
  }

  /**
   * Get the time since a date
   *
   * @param date the date to get the time from
   * @return the time since a date
   */
  @NotNull
  public static Time getTimeFromToday(@NotNull LocalDateTime date) {
    return getTimeDifference(date, getLocalDateFromMillis(System.currentTimeMillis()));
  }

  /**
   * Get the time since an offset date
   *
   * @param date the date to get the time from
   * @return the time since a date
   */
  @NotNull
  public static Time getTimeFromToday(@NotNull OffsetDateTime date) {
    return getTimeDifference(date, getOffsetDateFromMillis(System.currentTimeMillis()));
  }

  /**
   * Get the time difference from two dates
   *
   * @param date the first date
   * @param compare the second date
   * @return the two dates to compare the time from
   */
  @NotNull
  public static Time getTimeDifference(
      @NotNull LocalDateTime date, @NotNull LocalDateTime compare) {
    long millis = getMillisFromLocalDate(date) - getMillisFromLocalDate(compare);
    if (millis < 0) {
      millis *= -1;
    }
    return Time.fromMillis(millis);
  }

  /**
   * Get the time difference from two offset dates
   *
   * @param date the first date
   * @param compare the second date
   * @return the two dates to compare the time from
   */
  public static Time getTimeDifference(
      @NotNull OffsetDateTime date, @NotNull OffsetDateTime compare) {
    long millis = getMillisFromOffsetDate(date) - getMillisFromOffsetDate(compare);
    if (millis < 0) {
      millis *= -1;
    }
    return Time.fromMillis(millis);
  }
}
