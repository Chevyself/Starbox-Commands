package me.googas.commands.jda.cooldown;

import lombok.NonNull;
import me.googas.commands.jda.permissions.Permit;
import me.googas.commands.time.Time;

/** Represents how cooldown should behave on a command. */
public enum CooldownBehaviour {
  /**
   * It is tight to the user. This means that the user can run the command once until it gets
   * cooldown
   */
  USER,
  /**
   * It is tight to the member. This means that the user can run the command once per {@link
   * net.dv8tion.jda.api.entities.Guild}
   */
  MEMBER;

  /**
   * Create a cooldown manager. This creates a manager based on the type
   *
   * @param cooldown the time that the command needs to cooldown
   * @param permission the permission which users may have to not require cooldown
   * @return the new manager instance
   */
  @NonNull
  public JdaCooldownManager create(@NonNull Time cooldown, Permit permission) {
    switch (this) {
      case USER:
        return new UserCooldownManager(cooldown, permission);
      case MEMBER:
        return new MemberCooldownManager(cooldown, permission);
      default:
        throw new IllegalStateException(this + " is not a valid Cooldown Behaviour");
    }
  }
}
