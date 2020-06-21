package com.starfishst.bukkit.targets.argument.parser;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.exception.SelectorArgumentException;
import com.starfishst.bukkit.targets.TargetRandomPlayer;
import com.starfishst.bukkit.targets.argument.TargetArgument;
import java.util.LinkedHashMap;
import org.jetbrains.annotations.NotNull;

/** Parses the arguments for the target selector */
public interface ArgumentParser {

  /**
   * Gets the arguments inside a string
   *
   * @param string the strings to get the arguments
   * @param context the context of the command
   * @return the parsed arguments and their respective values
   * @throws SelectorArgumentException if the arguments are wrong
   */
  @NotNull
  LinkedHashMap<TargetArgument, String> fromString(
      @NotNull String string, @NotNull CommandContext context) throws SelectorArgumentException;

  /**
   * Get the target of a random player
   *
   * @param string the string of the argument to parse
   * @param context the context of the command
   * @return the target select of a random player
   * @throws SelectorArgumentException if the arguments are wrong
   */
  @NotNull
  TargetRandomPlayer random(@NotNull String string, @NotNull CommandContext context)
      throws SelectorArgumentException;
}
