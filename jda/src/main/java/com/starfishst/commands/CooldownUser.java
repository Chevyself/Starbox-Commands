package com.starfishst.commands;

import com.starfishst.core.utils.cache.Catchable;
import com.starfishst.core.utils.time.Time;
import org.jetbrains.annotations.NotNull;

/** An user that is inside the cooldown of a command */
public class CooldownUser extends Catchable {

  /** The id of the user */
  private final long id;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the user from the cooldown
   * @param id the id of the user
   */
  public CooldownUser(@NotNull Time toRemove, long id) {
    super(toRemove);
    this.id = id;
  }

  /**
   * Get the id of the user
   *
   * @return the id of the user
   */
  public long getId() {
    return id;
  }

  @Override
  public void onSecondsPassed() {}

  @Override
  public void onRemove() {}
}
