package com.github.chevyself.starbox.annotations;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.util.Strings;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
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

  class Supplier {

    @NonNull
    public static String getUsage(
        @NonNull AnnotatedElement element,
        @NonNull List<Argument<?>> arguments,
        @NonNull String... aliases) {
      String usage = null;
      if (element.isAnnotationPresent(Command.class)) {
        usage = element.getAnnotation(Command.class).usage();
      }
      if (usage == null || usage.isEmpty()) {
        usage = "/" + Strings.buildUsageAliases(aliases) + " " + Argument.generateUsage(arguments);
      }
      return usage;
    }

    @NonNull
    public static String getUsage(@NonNull AnnotatedElement element, @NonNull String... aliases) {
      return Supplier.getUsage(element, new ArrayList<>(), aliases);
    }

    @NonNull
    public static String getDescription(@NonNull AnnotatedElement element) {
      if (element.isAnnotationPresent(Command.class)) {
        return element.getAnnotation(Command.class).description();
      }
      return "No description provided";
    }
  }
}
