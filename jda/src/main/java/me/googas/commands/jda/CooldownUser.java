package me.googas.commands.jda;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.time.Time;
import me.googas.starbox.time.Unit;

/** An user that is inside the cooldown of a command */
public class CooldownUser {

  /** The time when the user can execute the command again */
  @Getter private final long expires;
  /** The id of the user */
  @Getter private final long id;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the user from the cooldown
   * @param id the id of the user
   */
  public CooldownUser(@NonNull Time toRemove, long id) {
    this.expires = System.currentTimeMillis() + toRemove.millis();
    this.id = id;
  }

  /**
   * Get whether the user can execute the command again
   *
   * @return true if the cooldown time has expired
   */
  public boolean isExpired() {
    return expires < System.currentTimeMillis();
  }

  /**
   * Get the time in which the user can execute the command again
   *
   * @return the time in which the user can execute the command again
   */
  @NonNull
  public Time getTimeLeft() {
    if (this.isExpired()) return new Time(0, Unit.SECONDS);
    return Time.fromMillis(expires - System.currentTimeMillis());
  }
}
