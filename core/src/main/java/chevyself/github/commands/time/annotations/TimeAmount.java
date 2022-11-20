package chevyself.github.commands.time.annotations;

import chevyself.github.commands.time.TimeUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import lombok.NonNull;

/** This represents {@link java.time.Duration} as an annotation. */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAmount {

  /**
   * Get the value of the amount of time. This is used {@link java.time.Duration#of(long,
   * TemporalUnit)}
   *
   * @return the value of the amount of time
   */
  long amount() default 0;

  /**
   * Get the unit of the amount of time. This is used {@link java.time.Duration#of(long,
   * TemporalUnit)}
   *
   * @return the unit of the amount of time
   */
  ChronoUnit unit() default ChronoUnit.MILLIS;

  /**
   * Get the {@link String} to forName the amount of time instead of using {@link #amount()} and
   * {@link #unit()}.
   *
   * @return the {@link String} that can be parsed using {@link TimeUtil#durationOf(String)}
   */
  @NonNull
  String value() default "";
}
