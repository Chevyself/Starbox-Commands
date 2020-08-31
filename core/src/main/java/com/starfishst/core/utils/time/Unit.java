package com.starfishst.core.utils.time;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/** Represents a time unit */
public enum Unit {
  /** The unit of milliseconds */
  MILLISECONDS("l", "millis", true, 1),
  /** The unit of Minecraft ticks */
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
   * @param millis the unit milliseconds
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
   * @throws IllegalArgumentException if the string did not match an unit.
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
   * Get the unit using a char, this means using the {@link #simple}
   *
   * @param charAt the char to get the unit from
   * @return the matched unit
   * @throws IllegalArgumentException if the character did not match an unit
   */
  public static Unit fromChar(final char charAt) {
    return Unit.fromString(String.valueOf(charAt));
  }

  /**
   * Get a unit using milliseconds. This will loop to get the highest unit with the milliseconds
   * given this means that:
   *
   * <p>If the parameter is 300,000 the given unit would be {@link #MINUTES} as it is lower but the
   * highest from the lower units.
   *
   * <p>This method ignores the units with {@link #millisObtainable} as false.
   *
   * @param millis the milliseconds to get the unit from
   * @return the unit given by the millis
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
   * Get an unit using a {@link TimeUnit}. It will use the {@link #fromMillis(long)} and the millis
   * used are given using {@link TimeUnit#toMillis(long)} using a duration of "1"
   *
   * @param unit the java unit to get this type of unit from
   * @return the unit that matches the milliseconds given by the java unit
   */
  public static Unit fromTimeUnit(@NotNull TimeUnit unit) {
    return fromMillis(unit.toMillis(1));
  }

  /**
   * Get whether this unit can be obtained from milliseconds
   *
   * @return true if it can be obtained from milliseconds
   */
  public boolean isMillisObtainable() {
    return millisObtainable;
  }

  /**
   * Get this unit millis
   *
   * @return this unit millis
   */
  public long millis() {
    return this.millis;
  }

  /**
   * Get this unit millis multiplied by the duration. Simple as it is this unit {@link #millis}
   * multiplied by the duration
   *
   * @param duration to multiply this unit millis to
   * @return the millis of the unit given the duration
   */
  public long millis(long duration) {
    return this.millis * duration;
  }

  /**
   * Get this unit as {@link TimeUnit}. It will do a loop similar that the one made in {@link
   * #fromMillis(long)} but given the {@link TimeUnit#values()}
   *
   * @return the time unit given by this unit
   */
  public TimeUnit toTimeUnit() {
    TimeUnit result = TimeUnit.NANOSECONDS;
    for (TimeUnit value : TimeUnit.values()) {
      if (value.toMillis(1) <= this.millis) {
        result = value;
      }
    }
    return result;
  }

  /**
   * Get the single character representation of the unit
   *
   * @return the {@link #simple} of the unit
   */
  @NotNull
  public String getSimple() {
    return this.simple;
  }

  /**
   * Get the complete name of the unit
   *
   * @return the {@link #complete} of the unit
   */
  @NotNull
  public String getComplete() {
    return this.complete;
  }
}
