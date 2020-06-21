package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** '@p' This allows to target the nearest player in a command execution */
public class TargetNearestPlayer implements TargetPlayer {

  /** The targets that are nearer */
  @NotNull private final List<Player> targets;

  /**
   * Create the target selector
   *
   * @param targets the players that were nearer
   */
  public TargetNearestPlayer(@NotNull List<Player> targets) {
    this.targets = targets;
  }

  @NotNull
  @Override
  public List<Player> getTargets() {
    return targets;
  }
}
