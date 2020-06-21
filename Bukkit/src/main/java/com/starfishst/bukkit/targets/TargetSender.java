package com.starfishst.bukkit.targets;

import com.starfishst.core.utils.Lots;
import java.util.List;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/** '@s' This targets the entity executing the command */
public class TargetSender implements TargetSelector {

  /** The entity that send the command */
  @NotNull private final Entity sender;

  /**
   * Create the target sender
   *
   * @param sender the sender of the command
   */
  public TargetSender(@NotNull Entity sender) {
    this.sender = sender;
  }

  @Override
  public List<Entity> getTargets() {
    return Lots.inmutable(sender);
  }
}
