package com.starfishst.core.utils.time;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Simple class for timing */
public class Time {

  /** The value of time */
  private final long value;
  /** The unit of time */
  @NotNull private final Unit unit;

  /**
   * Create a time instance
   *
   * @param value the value of time
   * @param unit the unit of time
   */
  public Time(final long value, @NotNull final Unit unit) {
    this.value = value;
    this.unit = unit;
  }

  /**
   * Parse a string of time: Should look like '1s' -> one second
   *
   * @param string the string to parse
   * @return a new time instance if correctly formatted
   */
  public static Time fromString(@Nullable final String string) {
    if (string != null && !string.isEmpty()) {
      final long value = Long.parseLong(string.substring(0, string.length() - 1));
      final Unit unit = Unit.fromChar(string.charAt(string.length() - 1));
      return new Time(value, unit);
    } else {
      throw new IllegalArgumentException("Cannot get time from a null string");
    }
  }

  /**
   * Get a time instance using millis
   *
   * @param millis the millis
   * @return the time
   */
  public static Time fromMillis(long millis) {
    return new Time(millis, Unit.fromMillis(millis));
  }

  /**
   * Get this same time but in other unit. It will also change the value depending on the unit
   * provided
   *
   * @param unit the unit to get the time from
   * @return the time with the new unit
   */
  public Time getAs(@NotNull final Unit unit) {
    return new Time(this.millis() / unit.millis(), unit);
  }

  /**
   * For minecraft purposes this will change the time into minecraft ticks
   *
   * @return the time in ticks
   */
  public long ticks() {
    return (millis() * 20) / 1000;
  }

  /**
   * Get the time in millis
   *
   * @return the time in millis
   */
  public long millis() {
    return this.value * this.unit.millis();
  }

  /**
   * Get the next date since today using this time
   *
   * @return the next date
   */
  public LocalDateTime nextDate() {
    return TimeUtils.getLocalDateFromMillis(nextDateMillis());
  }

  /**
   * Get the previous date since today using this time
   *
   * @return the previous date
   */
  public LocalDateTime previousDate() {
    return TimeUtils.getLocalDateFromMillis(previousDateMillis());
  }

  /**
   * Get the next offset date since today using this time
   *
   * @return the next date
   */
  public OffsetDateTime nextDateOffset() {
    return TimeUtils.getOffsetDateFromMillis(nextDateMillis());
  }

  /**
   * s Get the previous offset date since today using this time
   *
   * @return the previous date
   */
  public OffsetDateTime previousDateOffset() {
    return TimeUtils.getOffsetDateFromMillis(previousDateMillis());
  }

  /**
   * Get the next date since today using this time
   *
   * @return the next date in millis
   */
  private long nextDateMillis() {
    return System.currentTimeMillis() + this.millis();
  }

  /**
   * Get the previous date since today using this time
   *
   * @return the previous date in millis
   */
  private long previousDateMillis() {
    return System.currentTimeMillis() - this.millis();
  }

  /**
   * This means that if there is 60 seconds the effective string will be 1 minute
   *
   * @return the effective string
   */
  @NotNull
  public String toEffectiveString() {
    Time effectiveTime = null;
    for (Unit unit : Unit.values()) {
      if (unit.millis() < this.millis()) {
        effectiveTime = this.getAs(unit);
      }
    }
    if (effectiveTime != null) {
      return effectiveTime.toString();
    } else {
      return this.toString();
    }
  }

  /**
   * Is a string that can be used in {@link Time#fromString(String)}
   *
   * @return the string
   */
  @NotNull
  public String toDatabaseString() {
    return value + unit.getSimple();
  }

  /**
   * Get the time as {@link ClassicTime}
   *
   * @return the time as classic time
   */
  @NotNull
  public ClassicTime toClassicTime() {
    TimeUnit newUnit = unit.toTimeUnit();
    long newValue = getAs(newUnit);
    return new ClassicTime(newValue, newUnit);
  }

  /**
   * Just like {@link Time#getAs(Unit)} but using java unit
   *
   * @param unit the java unit to get the value from
   * @return the value as certain unit
   */
  private long getAs(@NotNull TimeUnit unit) {
    return getAs(Unit.fromTimeUnit(unit)).getAs();
  }

  /**
   * Get the value of the time
   *
   * @return the value of the time
   */
  public long getAs() {
    return value;
  }

  /**
   * Get the unit of time
   *
   * @return the unit of time
   */
  @NotNull
  public Unit getUnit() {
    return unit;
  }

  @Override
  public String toString() {
    return this.value + " " + unit.toString().toLowerCase();
  }

  @NotNull
  public Time sustract(@NotNull Time time) {
    long millis = this.millis() - time.millis();
    return Time.fromMillis(millis < 0 ? 0 : millis);
  }

  @NotNull
  public Time sum(@NotNull Time time) {
    return Time.fromMillis(millis() + time.millis());
  }
}
