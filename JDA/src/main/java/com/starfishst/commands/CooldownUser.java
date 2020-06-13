package com.starfishst.commands;

import com.starfishst.core.utils.cache.Catchable;
import com.starfishst.core.utils.time.Time;
import org.jetbrains.annotations.NotNull;

/** An user that is inside the cooldown of a command */
public class CooldownUser extends Catchable {

  /** The command where the user is cooled down */
  @NotNull private final AnnotatedCommand command;
  /** The id of the user */
  private final long id;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the user from the cooldown
   * @param command the command where the user is cooled down
   * @param id the id of the user
   */
  public CooldownUser(@NotNull Time toRemove, @NotNull AnnotatedCommand command, long id) {
    super(toRemove);
    this.command = command;
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

  /**
   * Get the command where the user is cooled down
   *
   * @return the command
   */
  @NotNull
  public AnnotatedCommand getCommand() {
    return command;
  }

  @Override
  public void onSecondsPassed() {}

  @Override
  public void onRemove() {}
}
