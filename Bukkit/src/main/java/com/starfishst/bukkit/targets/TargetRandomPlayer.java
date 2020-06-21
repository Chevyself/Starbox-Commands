package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Target a random player */
public class TargetRandomPlayer implements TargetPlayer {

  /** The targets of the random variable */
  @NotNull private final List<Player> targets;

  /**
   * Create the target selector
   *
   * @param targets the players that were targeted on it
   */
  public TargetRandomPlayer(@NotNull List<Player> targets) {
    this.targets = targets;
  }

  @NotNull
  @Override
  public List<Player> getTargets() {
    return targets;
  }
}
