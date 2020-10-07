package com.starfishst.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.dv8tion.jda.api.Permission;

/**
 * This annotations gives the permission for a command. This in order to have another way of having
 * permissions
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Perm {

  /**
   * Get the permission as a string node. If this is left empty then it will use the {@link}
   *
   * @return the string permission
   */
  String node() default "";

  /**
   * Get the permission as a discord permission. IF this is left in {@link Permission#UNKNOWN} and
   * the permission node is empty then the command will not have permission
   *
   * @return the discord permission
   */
  Permission permission() default Permission.UNKNOWN;
}
