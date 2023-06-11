package com.github.chevyself.starbox.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandPermission {

  String value();

  class Supplier {
    public static String getPermission(@NonNull AnnotatedElement element) {
      if (element.isAnnotationPresent(CommandPermission.class)) {
        return element.getAnnotation(CommandPermission.class).value();
      }
      return null;
    }
  }
}
