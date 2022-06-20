package me.googas.commands.jda.cooldown;

import lombok.NonNull;
import me.googas.commands.time.Time;

public enum CooldownBehaviour {
  USER,
  MEMBER;

  @NonNull
  public JdaCooldownManager create(@NonNull Time cooldown) {
    switch (this) {
      case USER:
        return new UserCooldownManager(cooldown);
      case MEMBER:
        return new MemberCooldownManager(cooldown);
      default:
        throw new IllegalStateException(this + " is not a valid Cooldown Behaviour");
    }
  }
}
