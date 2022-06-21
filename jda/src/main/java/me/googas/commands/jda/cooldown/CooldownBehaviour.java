package me.googas.commands.jda.cooldown;

import lombok.NonNull;
import me.googas.commands.jda.permissions.Permit;
import me.googas.commands.time.Time;

public enum CooldownBehaviour {
  USER,
  MEMBER;

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
