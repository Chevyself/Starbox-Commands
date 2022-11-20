package chevyself.github.commands.bukkit.annotations;

import chevyself.github.commands.bukkit.CooldownManager;
import chevyself.github.commands.time.annotations.TimeAmount;
import lombok.NonNull;

/** Annotation to create {@link CooldownManager} for commands. */
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
