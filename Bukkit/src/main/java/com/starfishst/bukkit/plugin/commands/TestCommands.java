package com.starfishst.bukkit.plugin.commands;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.result.Result;
import com.starfishst.core.annotations.Optional;
import com.starfishst.core.annotations.Required;
import com.starfishst.core.utils.math.geometry.Box;
import com.starfishst.core.utils.math.geometry.Cylinder;
import com.starfishst.core.utils.math.geometry.Point;
import com.starfishst.core.utils.math.geometry.Shape;
import com.starfishst.core.utils.math.geometry.Sphere;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommands {

  @NotNull private final HashMap<UUID, Clipboard> clipboards = new HashMap<>();

  /*
  @Command(aliases = "sphere", description = "Create a sphere")
  public Result test(
      Player player,
      @Required(name = "id", description = "The id of  the sphere") String id,
      @Required(name = "radius", description = "The radius of the sphere") double radius,
      @Optional(
              name = "blocks",
              description = "Whether to placeblocks or armor stands",
              suggestions = {"false", "true"})
          boolean blocks) {
    Location location = player.getLocation();
    Sphere sphere =
        new Sphere(id, new Point(location.getX(), location.getY(), location.getZ()), radius);
    if (location.getWorld() == null) {
      return new Result("World null");
    }
    if (!blocks) {
      sphere
          .getFacePoints()
          .forEach(
              point ->
                  location
                      .getWorld()
                      .spawn(
                          new Location(
                              location.getWorld(), point.getX(), point.getY(), point.getZ()),
                          ArmorStand.class,
                          armorStand -> {
                            armorStand.setGravity(false);
                            armorStand.setInvulnerable(true);
                            armorStand.setVisible(false);
                            armorStand.addPotionEffect(
                                new PotionEffect(
                                    PotionEffectType.GLOWING,
                                    Integer.MAX_VALUE,
                                    0,
                                    true,
                                    false,
                                    true));
                          }));
    } else {
      sphere
          .getFacePoints()
          .forEach(
              point -> {
                location
                    .getWorld()
                    .getBlockAt(
                        new Location(location.getWorld(), point.getX(), point.getY(), point.getZ()))
                    .setType(Material.GLASS);
              });
    }
    return new Result();
  }
     */

  @Command(aliases = "pos1", description = "Sets the position 1")
  public Result pos1(Player player) {
    Clipboard clipboard = this.clipboards.get(player.getUniqueId());
    Location location = player.getLocation();
    double x = Math.ceil(location.getX());
    double y = Math.ceil(location.getY());
    double z = Math.ceil(location.getZ());
    Point point = new Point(x, y, z);
    if (clipboard == null) {
      clipboard = new Clipboard(null, null);
      this.clipboards.put(player.getUniqueId(), clipboard);
    }
    clipboard.setPos1(point);
    return new Result("&ePosition 1 has been set to " + x + ", " + y + ", " + z);
  }

  @Command(aliases = "pos2", description = "Sets the position 2")
  public Result pos2(Player player) {
    Point point = getPoint(player);
    Clipboard clipboard = getClipboard(player);
    clipboard.setPos2(point);
    return new Result(
        "&ePosition 2 has been set to " + point.getX() + ", " + point.getY() + ", " + point.getZ());
  }

  @Command(aliases = "set", description = "Set the block in the region")
  public Result set(
      Player player,
      @Required(name = "material", description = "The material to set the region to")
          Material material,
      @Optional(
              name = "faces",
              description = "Whether to only change only the faces",
              suggestions = {"false", "true"})
          boolean faces) {
    Clipboard clipboard = getClipboard(player);
    if (clipboard.getPos1() == null) {
      return new Result("&cPlease set the position 1");
    }
    if (clipboard.getPos2() == null) {
      return new Result("&cPlease set the position 2");
    }
    Box box = new Box(clipboard.getPos1(), clipboard.getPos2(), null);
    return changeBlocks(player, material, box, faces);
  }

  @Command(aliases = "sphere", description = "Create an sphere")
  public Result sphere(
      Player player,
      @Required(name = "material", description = "The material in which the sphere should made of")
          Material material,
      @Required(name = "radius", description = "The radius of the sphere") double radius,
      @Optional(
              name = "faces",
              description = "Whether the blocks that should change should only be the faces",
              suggestions = {"false", "true"})
          boolean faces) {
    Sphere sphere = new Sphere(null, getPoint(player), radius);
    return changeBlocks(player, material, sphere, faces);
  }

  @Command(aliases = "cylinder", description = "Create a cylinder")
  public Result cylinder(
      Player player,
      @Required(
              name = "material",
              description = "The material in which the cylinder should made of")
          Material material,
      @Required(name = "radius", description = "The radius of the cylinder") double radius,
      @Required(name = "height", description = "The height of the cylinder") double height,
      @Optional(
              name = "faces",
              description = "Whether the blocks that should change should only be the faces",
              suggestions = {"false", "true"})
          boolean faces) {
    Cylinder cylinder = new Cylinder(null, getPoint(player), radius, height);
    return changeBlocks(player, material, cylinder, faces);
  }

  public Result changeBlocks(
      @NotNull Player player, @NotNull Material material, @NotNull Shape shape, boolean faces) {
    List<Point> points;
    if (faces) {
      points = shape.getFacePoints();
    } else {
      points = shape.getPointsInside();
    }
    World world = player.getLocation().getWorld();
    if (world == null) {
      return new Result("&cYou are not in a legal world!");
    }
    points.forEach(
        point -> {
          world
              .getBlockAt(new Location(world, point.getX(), point.getY(), point.getZ()))
              .setType(material);
        });
    return new Result("&eBlocks have been changed");
  }

  /**
   * Get the clipboard of a player
   *
   * @param player the player that needs the clipboard
   * @return the clipboard
   */
  @NotNull
  public Clipboard getClipboard(Player player) {
    Clipboard clipboard = this.clipboards.get(player.getUniqueId());
    if (clipboard == null) {
      clipboard = new Clipboard(null, null);
      this.clipboards.put(player.getUniqueId(), clipboard);
    }
    return clipboard;
  }

  /**
   * Get the point where the player is standing
   *
   * @param player the player to get the point
   * @return the point
   */
  @NotNull
  public Point getPoint(@NotNull Player player) {
    Location location = player.getLocation();
    double x = Math.ceil(location.getX());
    double y = Math.ceil(location.getY());
    double z = Math.ceil(location.getZ());
    return new Point(x, y, z);
  }
}
