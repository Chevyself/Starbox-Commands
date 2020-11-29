package com.starfishst.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

/** This marks a parameter as required. It can be think in a way as {@link NonNull} */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Required {

  /** @return The name of the argument */
  @NonNull
  String name() default "No name provided";

  /** @return The description of the argument */
  @NonNull
  String description() default "No description provided";

  /** @return The default value of the optional argument */
  @NonNull
  String[] suggestions() default {};
}
