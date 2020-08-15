package com.starfishst.core.utils.time;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/** Represents a time unit */
public enum Unit {
  /** The unit of milliseconds */
  MILLISECONDS("l", "millis", true, 1),
  /** Minecraft ticks */
  MINECRAFT_TICKS("t", "ticks", false, 50),
  /** The unit of seconds */
  SECONDS("s", "seconds", true, 1000),
  /** The unit of minutes */
  MINUTES("m", "minutes", true, 60000),
  /** The unit of hours */
  HOURS("h", "hours", true, 3600000),
  /** The unit of days */
  DAYS("d", "days", true, 86400000),
  /** The unit of weeks */
  WEEKS("w", "weeks", true, 604800000),
  /** The unit of months */
  MONTHS("o", "months", true, 2629800000L),
  /** The unit of years */
  YEARS("y", "years", true, 31557600000L);

  /** The unit represented by a single letter/character */
  @NotNull private final String simple;
  /** The unit as a full name */
  @NotNull private final String complete;
  /** Whether it can be obtained from a {@link Unit#fromMillis(long)} query */
  @NotNull private final boolean millisObtainable;
  /** The unit in millis */
  private final long millis;

  /**
   * This unit has a 'simple' denomination which is a single character and a 'complete' which is
   * represented by a word in the english dictionary
   *
   * @param simple the simple denomination
   * @param complete the complete denomination
   * @param millisObtainable Whether it can be obtained from a {@link Unit#fromMillis(long)} query
   * @param millis the unit in millis
   */
  Unit(@NotNull String simple, @NotNull String complete, boolean millisObtainable, long millis) {
    this.simple = simple;
    this.complete = complete;
    this.millisObtainable = millisObtainable;
    this.millis = millis;
  }

  /**
   * Get the unit from a string. You can use either the {@link Unit#simple} or {@link Unit#complete}
   *
   * @param string the string to get the unit from
   * @return the matched string
   */
  @NotNull
  private static Unit fromString(final String string) {
    for (Unit unit : Unit.values()) {
      if (unit.simple.equalsIgnoreCase(string) || unit.complete.equalsIgnoreCase(string)) {
        return unit;
      }
    }
    throw new IllegalArgumentException(string + " is not a valid unit type");
  }

  /**
   * Get the unit using a char, this means using the simple of the unit
   *
   * @param charAt the char to get the unit from
   * @return the unit
   */
  public static Unit fromChar(final char charAt) {
    return Unit.fromString(String.valueOf(charAt));
  }

  /**
   * Get a unit using milliseconds
   *
   * @param millis the milliseconds to get the unit from
   * @return the unit
   */
  public static Unit fromMillis(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    } else {
      Unit unit = MILLISECONDS;
      for (Unit value : values()) {
        if (value.millis <= millis && value.isMillisObtainable()) {
          unit = value;
        }
      }
      return unit;
    }
  }

  /**
   * Get whether it can be obtained from milliseconds
   *
   * @return true if it can be obtained from milliseconds
   */
  public boolean isMillisObtainable() {
    return millisObtainable;
  }

  /**
   * Get a unit using a classic java unit
   *
   * @param unit the classic java unit
   * @return the unit
   */
  public static Unit fromTimeUnit(@NotNull TimeUnit unit) {
    return fromMillis(unit.toMillis(1));
  }

  /**
   * Get the unit millis
   *
   * @return the unit millis
   */
  public long millis() {
    return this.millis;
  }

  /**
   * Get this unit as a classic java class
   *
   * @return the time unit
   */
  public TimeUnit toTimeUnit() {
    TimeUnit result = TimeUnit.NANOSECONDS;
    for (TimeUnit value : TimeUnit.values()) {
      if (value.toMillis(1) <= millis) {
        result = value;
      }
    }
    return result;
  }

  /**
   * The single character representation of the unit
   *
   * @return the simple of the unit
   */
  @NotNull
  public String getSimple() {
    return simple;
  }

  /**
   * Get the complete name of the unit
   *
   * @return the name of the unit
   */
  @NotNull
  public String getComplete() {
    return complete;
  }
}
