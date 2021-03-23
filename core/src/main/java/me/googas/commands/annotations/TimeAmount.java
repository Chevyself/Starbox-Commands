package me.googas.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.NonNull;
import me.googas.commands.time.unit.EasyUnit;
import me.googas.commands.time.unit.Unit;

/**
 * This represents {@link me.googas.commands.time.Time} as an annotation
 *
 * <p>See {@link me.googas.commands.time.Time#of(TimeAmount)} to know how it is parsed
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAmount {

  /**
   * Get the value of the amount of time. This is used {@link
   * me.googas.commands.time.Time#of(double, EasyUnit)}
   *
   * @return the value of the amount of time
   */
  double value() default 0;

  /**
   * Get the unit of the amount of time. This is used {@link me.googas.commands.time.Time#of(double,
   * EasyUnit)}
   *
   * @return the unit of the amount of time
   */
  Unit unit() default Unit.MILLIS;

  /**
   * Get the {@link String} to parse the amount of time instead of using {@link #value()} and {@link
   * #unit()}
   *
   * @return the {@link String} that can be parsed using {@link
   *     me.googas.commands.time.Time#parse(String, boolean)}
   */
  @NonNull
  String string() default "";
}
