package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Entity;

/**
 * A target selector is minecraft is used to point towards entities in a command This represents all
 * of them:
 */
public interface TargetSelector {

  /**
   * Get the targeted entities
   *
   * @return the target entities
   */
  List<? extends Entity> getTargets();
}
