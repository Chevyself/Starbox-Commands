package com.github.chevyself.starbox.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHelp {
  String description();

  class Supplier {

    @NonNull
    public static String getDescription(@NonNull AnnotatedElement element) {
      CommandHelp annotation = element.getAnnotation(CommandHelp.class);
      if (annotation == null) {
        return "No description provided";
      }
      return annotation.description();
    }
  }
}
