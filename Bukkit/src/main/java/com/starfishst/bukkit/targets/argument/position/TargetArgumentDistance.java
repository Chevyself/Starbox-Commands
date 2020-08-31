package com.starfishst.bukkit.targets.argument.position;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.exception.SelectorArgumentException;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.targets.argument.TargetArgument;
import com.starfishst.core.utils.math.geometry.Point;
import java.util.LinkedHashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class TargetArgumentDistance implements TargetArgument {

  /** The provider of the error messages */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create the handler for the distance argument
   *
   * @param messagesProvider the message provider for errors
   */
  public TargetArgumentDistance(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  /**
   * Parses a double to be used
   *
   * @param string the string to be parsed as a double
   * @param context the context of the command
   * @return the parsed double
   * @throws SelectorArgumentException if the double cannot be parsed
   */
  protected double parseDouble(@NotNull String string, @NotNull CommandContext context)
      throws SelectorArgumentException {
    if (string.isEmpty()) {
      throw new SelectorArgumentException(messagesProvider.emptyDouble(context));
    } else {
      try {
        return Double.parseDouble(string);
      } catch (NumberFormatException e) {
        throw new SelectorArgumentException(messagesProvider.invalidDouble(string, context));
      }
    }
  }

  @Override
  public void apply(
      @NotNull List<? extends Entity> targets,
      @NotNull String value,
      @NotNull LinkedHashMap<TargetArgument, String> arguments,
      @NotNull CommandContext context)
      throws SelectorArgumentException {
    double x =
        parseDouble(
            arguments.getOrDefault(
                "x",
                context.getSender() instanceof Entity
                    ? String.valueOf(((Entity) context.getSender()).getLocation().getX())
                    : "0"),
            context);
    double y =
        parseDouble(
            arguments.getOrDefault(
                "y",
                context.getSender() instanceof Entity
                    ? String.valueOf(((Entity) context.getSender()).getLocation().getY())
                    : "0"),
            context);
    double z =
        parseDouble(
            arguments.getOrDefault(
                "z",
                context.getSender() instanceof Entity
                    ? String.valueOf(((Entity) context.getSender()).getLocation().getZ())
                    : "0"),
            context);
    double minDistance = -1;
    double maxDistance = -1;
    String distance = arguments.get("distance");
    if (distance != null) {
      if (distance.contains("..")) {
        String[] split = distance.split("..");
        minDistance = parseDouble(split[0], context);
        maxDistance = parseDouble(split[1], context);
      } else {
        maxDistance = parseDouble(distance, context);
      }
    }
    Point point = new Point(x, y, z);
  }

  @Override
  public @NotNull List<String> getAliases() {
    return null;
  }

  @Override
  public int getPriority() {
    return 0;
  }
}
