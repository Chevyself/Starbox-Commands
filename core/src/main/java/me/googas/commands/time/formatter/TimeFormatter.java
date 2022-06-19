package me.googas.commands.time.formatter;

import lombok.NonNull;
import me.googas.commands.time.Time;

/**
 * Implementations of this class convert {@link Time} into a readable {@link String} such as {@link
 * me.googas.commands.time.formatter.HhMmSsFormatter} which converts the given instance of time into
 * 'Hh:Mm:Ss'
 *
 * <p>Implement this class to format {@link Time} at your liking
 *
 * @see me.googas.commands.time.formatter.HhMmSsFormatter
 */
public interface TimeFormatter {

  /**
   * Format the given time into a readable {@link String}.
   *
   * @param time the instance of time to convert
   * @return the formatted string
   */
  @NonNull
  String format(@NonNull Time time);
}
