package me.googas.commands.time.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.NonNull;
import me.googas.commands.time.Time;
import me.googas.commands.time.unit.StarboxUnit;
import me.googas.commands.time.unit.Unit;

/** This represents {@link Time} as an annotation. */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAmount {

  /**
   * Get the value of the amount of time. This is used {@link Time#of(double, StarboxUnit)}
   *
   * @return the value of the amount of time
   */
  double value() default 0;

  /**
   * Get the unit of the amount of time. This is used {@link Time#of(double, StarboxUnit)}
   *
   * @return the unit of the amount of time
   */
  Unit unit() default Unit.MILLIS;

  /**
   * Get the {@link String} to forName the amount of time instead of using {@link #value()} and
   * {@link #unit()}.
   *
   * @return the {@link String} that can be parsed using {@link Time#parse(String, boolean)}
   */
  @NonNull
  String string() default "";
}
