package me.googas.commands.bukkit.annotations;

import lombok.NonNull;
import me.googas.commands.time.annotations.TimeAmount;

/** Annotation to create {@link me.googas.commands.bukkit.CooldownManager} for commands. */
public @interface Cooldown {

  /**
   * Get the permission node that overrides the cooldown.
   *
   * @return the permission node
   */
  @NonNull
  String permission() default "";

  /**
   * Get the time that the commands needs to cooldown before being executed again.
   *
   * @return the time
   */
  @NonNull
  TimeAmount time() default @TimeAmount;
}
