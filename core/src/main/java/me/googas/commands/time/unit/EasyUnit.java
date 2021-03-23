package me.googas.commands.time.unit;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import lombok.NonNull;
import me.googas.commands.time.Time;

/**
 * An unit of time such as seconds, minutes, days or years
 *
 * <p>The direct implementation to use in {@link me.googas.commands.time.Time} is {@link Unit} but
 * classes implementing this interface should represent an unit that is used to measure time.
 */
public interface EasyUnit extends TemporalUnit {

  /**
   * Get a single character which may be used to represent the unit such as:
   *
   * <ul>
   *   <li>'M' minute
   *   <li>'S' second
   *   <li>'O' month
   *   <li>'Y' year
   * </ul>
   *
   * @return the single character that represents the unit
   */
  char getSingle();

  /**
   * Get this unit as millis such as:
   *
   * <ul>
   *   <li>second = 1000
   *   <li>minute = 60000
   *   <li>year = 31557600000L
   * </ul>
   *
   * @return the millis that represent the unit
   */
  long getMillis();

  /**
   * Get the duration in millis of the given value
   *
   * <p>For example:
   *
   * <pre>
   * double value = Unit.SECONDS.getMillis(2);
   * System.out.println("Output 2000: " + value);
   * </pre>
   *
   * This divides the parameter value with {@link #getMillis()}
   *
   * @param value the value to get the duration in millis from
   * @return the duration in millis
   */
  default double getMillis(double value) {
    return value / this.getMillis();
  }

  /**
   * Get an instance of time based on this unit
   *
   * @return the instance of time based in this unit
   */
  @NonNull
  default Time getTime() {
    return Time.of(1, this);
  }

  @Override
  default <R extends Temporal> R addTo(R temporal, long amount) {
    temporal.plus(amount, this);
    return temporal;
  }

  @Override
  default long between(Temporal temporal, Temporal temporal1) {
    return temporal.until(temporal1, this);
  }
}
