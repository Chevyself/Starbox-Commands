package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** '@a' This targets all players */
public class TargetAllPlayers implements TargetPlayer {

  /** All the players */
  @NotNull private final List<Player> targets;

  /**
   * Create the target
   *
   * @param targets all the players that were a target
   */
  public TargetAllPlayers(@NotNull List<Player> targets) {
    this.targets = targets;
  }

  @Override
  public List<Player> getTargets() {
    return null;
  }
}
