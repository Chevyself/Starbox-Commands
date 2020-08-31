package com.starfishst.core.utils.time;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * Just like {@link Time} but using the java unit {@link TimeUnit}. Only created for getting the
 * unit and the value use {@link Time} for other time queries
 */
public class ClassicTime {

  /** The unit of the time */
  @NotNull private final TimeUnit unit;
  /** The value of the time */
  private final long value;

  /**
   * Create an instance. If we want the time to be "10 seconds" we would give the value as "10" and
   * the unit is {@link TimeUnit#SECONDS}
   *
   * @param value the value of time
   * @param unit the unit of time
   */
  public ClassicTime(long value, @NotNull TimeUnit unit) {
    this.value = value;
    this.unit = unit;
  }

  /**
   * Get the value of this time instance
   *
   * @return the value of this time instance
   */
  public long getValue() {
    return this.value;
  }

  /**
   * Get the unit of this time instance
   *
   * @return the unit of this time instance
   */
  @NotNull
  public TimeUnit getUnit() {
    return this.unit;
  }

  /**
   * Get this time instance in millis. Converted using {@link TimeUnit#toMillis(long)} given the
   * {@link #value}
   *
   * @return the classic time given in millis
   */
  public long millis() {
    return this.unit.toMillis(this.value);
  }

  /**
   * Get the time as {@link Time}. It will be converted using {@link Time#fromMillis(long)} and the
   * millis will be given by {@link #millis()}
   *
   * @return this instance a core type of time
   */
  @NotNull
  public Time toTime() {
    return Time.fromMillis(this.millis());
  }

  @Override
  public String toString() {
    return this.value + " " + unit.toString().toLowerCase();
  }
}
