package com.starfishst.bukkit.targets;

import java.util.List;
import org.bukkit.entity.Player;

/** This makes that the target selector only targets players and not every type of entity */
public interface TargetPlayer extends TargetSelector {

  @Override
  List<Player> getTargets();
}
