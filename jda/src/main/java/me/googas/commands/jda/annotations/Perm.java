package me.googas.commands.jda.annotations;

import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

public @interface Perm {

  @NonNull
  Permission value() default Permission.UNKNOWN;

  @NonNull
  String node() default "";
}
