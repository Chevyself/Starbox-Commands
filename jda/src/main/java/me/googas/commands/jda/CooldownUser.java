package me.googas.commands.jda;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;

/**
 * This is an instance of an {@link net.dv8tion.jda.api.entities.User} that is inside the {@link
 * JdaCommand} cooldown and cannot execute the command until {@link #isExpired()} = true
 */
public class CooldownUser {

  @Getter private final long expires;
  @Getter private final long id;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the user from the cooldown in getMillis {@link #expires}
   *     will be the result of this parameter + {@link System#currentTimeMillis()}
   * @param id the long id of the {@link net.dv8tion.jda.api.entities.User} {@link User#getIdLong()}
   */
  public CooldownUser(long toRemove, long id) {
    this.expires = System.currentTimeMillis() + toRemove;
    this.id = id;
  }

  /**
   * Get whether the user can execute the command again. This means that {@link #expires} is less
   * than {@link System#currentTimeMillis()}
   *
   * @return true if the cooldown time has expired
   */
  public boolean isExpired() {
    return this.expires < System.currentTimeMillis();
  }

  /**
   * Get the time in which the user can execute the command again in millis. This is the result from
   * {@link #expires} less the {@link System#currentTimeMillis()}
   *
   * @return the time in which the user can execute the command again
   */
  public long getTimeLeftMillis() {
    if (this.isExpired()) return 0;
    return this.expires - System.currentTimeMillis();
  }
}
