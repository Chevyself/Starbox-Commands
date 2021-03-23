package me.googas.commands.jda;

import lombok.Getter;
import lombok.NonNull;

/** An user that is inside the cooldown of a command */
public class CooldownUser {

  /** The time when the user can execute the command again */
  @Getter private final long expires;
  /** The id of the user */
  @Getter private final long id;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the user from the cooldown in getMillis
   * @param id the id of the user
   */
  public CooldownUser(long toRemove, long id) {
    this.expires = System.currentTimeMillis() + toRemove;
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
  public long getTimeLeft() {
    if (this.isExpired()) return 0;
    return expires - System.currentTimeMillis();
  }
}
