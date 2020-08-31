package com.starfishst.bukkit.targets.argument;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.exception.SelectorArgumentException;
import java.util.LinkedHashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/** There can be certain arguments when it comes to selecting a player or an entity */
public interface TargetArgument extends Comparable<TargetArgument> {

  /**
   * Applies the argument into the list of targets
   *
   * @param targets the targets to apply the argument
   * @param value the value of the argument
   * @param arguments the other arguments that could be needed for this one
   */
  void apply(
      @NotNull List<? extends Entity> targets,
      @NotNull String value,
      @NotNull LinkedHashMap<TargetArgument, String> arguments,
      @NotNull CommandContext context)
      throws SelectorArgumentException;

  /**
   * Get the aliases of the argument
   *
   * @return the aliases
   */
  @NotNull
  List<String> getAliases();

  /**
   * Get the priority in which the argument should be executed
   *
   * @return the priority
   */
  int getPriority();

  @Override
  default int compareTo(@NotNull TargetArgument argument) {
    return this.getPriority() - argument.getPriority();
  }
}
