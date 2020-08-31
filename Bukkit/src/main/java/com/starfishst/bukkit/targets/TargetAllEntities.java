package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/** '@e' This targets all entities */
public class TargetAllEntities implements TargetSelector {

  /** All the entities that were a target */
  @NotNull private final List<Entity> targets;

  /**
   * Create the target
   *
   * @param targets all the entities that were a target
   */
  public TargetAllEntities(@NotNull List<Entity> targets) {
    this.targets = targets;
  }

  @Override
  public List<Entity> getTargets() {
    return null;
  }
}
