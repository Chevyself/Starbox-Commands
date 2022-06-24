package me.googas.commands.jda.annotations;

import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/** Annotation that can be translated into a {@link me.googas.commands.jda.permissions.Permit}. */
public @interface Perm {

  /**
   * Get the value of the permission as a Discords'.
   *
   * @return the Discord permission
   */
  @NonNull
  Permission value() default Permission.UNKNOWN;

  /**
   * Get the node of the permission. In case that a custom permission system is required, it is
   * recommended to use one similar to Bukkit's which is based on {@link String} nodes and users may
   * or not have enabled the permission.
   *
   * @return the node
   */
  @NonNull
  String node() default "";
}
