package com.github.chevyself.starbox.annotations;

import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.middleware.Middleware;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Command {

  String[] aliases();

  String description() default "";

  String usage() default "";

  Flag[] flags() default {};

  @NonNull
  Class<? extends Middleware<?>>[] exclude() default {};

  @NonNull
  Class<? extends Middleware<?>>[] include() default {};
}
