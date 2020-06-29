package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.math.MathUtils;
import com.starfishst.core.utils.math.geometry.containers.Points;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A cylinder */
public class Cylinder implements Shape {

  /** The id of the shape */
  @Nullable private final String id;

  /** The base of the cylinder */
  @NotNull private Point base;
  /** The radius of the cylinder */
  private double radius;
  /** The height of the cylinder */
  private double height;

  /**
   * Create a cylinder
   *
   * @param id the id of the cylinder
   * @param base the base of the cylinder
   * @param radius the radius of the cylinder
   * @param height the height of the cylinder
   */
  public Cylinder(@Nullable String id, @NotNull Point base, double radius, double height) {
    this.id = id;
    this.base = base;
    this.radius = radius;
    this.height = height;
  }

  /**
   * Set the base of the cylinder
   *
   * @param base the new base
   */
  public void setBase(@NotNull Point base) {
    this.base = base;
  }

  /**
   * Set the radius of the cylinder
   *
   * @param radius the new radius
   */
  public void setRadius(double radius) {
    this.radius = radius;
  }

  /**
   * Set the height of the cylinder
   *
   * @param height the new height
   */
  public void setHeight(double height) {
    this.height = height;
  }

  /**
   * Get the base of the cylinder
   *
   * @return the base of the cylinder
   */
  @NotNull
  public Point getBase() {
    return base;
  }

  /**
   * Get the radius of the cylinder
   *
   * @return the radius
   */
  public double getRadius() {
    return radius;
  }

  /**
   * Get the height of the cylinder
   *
   * @return the height of the cylinder
   */
  public double getHeight() {
    return height;
  }

  /**
   * Get the height + the y coordinate of the base
   *
   * @return the total height
   */
  public double getTotalHeight() {
    return base.getY() + height;
  }

  /**
   * Get the minimum point of the cylinder
   *
   * @return the minimum point
   */
  @NotNull
  @Override
  public Point getMinimum() {
    return new Point(base.getX() - radius, base.getY(), base.getZ() - radius);
  }

  /**
   * Get the maximum point of the cylinder
   *
   * @return the maximum point
   */
  @NotNull
  @Override
  public Point getMaximum() {
    return new Point(base.getX() + radius, base.getY() + height, base.getZ() + radius);
  }

  @Nullable
  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public boolean contains(@NotNull Point point) {
    return point.getY() >= base.getY()
        && point.getY() <= getTotalHeight()
        && (MathUtils.square(point.getX() - base.getX())
                + MathUtils.square(point.getZ() - base.getZ())
            <= MathUtils.square(radius));
  }

  @Override
  public double getVolume() {
    return height * Math.PI * MathUtils.square(radius);
  }

  @NotNull
  @Override
  public Points getPointsInside() {
    return new Points(
        new Box(getMinimum(), getMaximum(), null)
            .getPointsInside().stream().filter(this::contains).collect(Collectors.toList()));
  }

  @Override
  public String toString() {
    return "Cylinder{"
        + "id='"
        + id
        + '\''
        + ", base="
        + base
        + ", radius="
        + radius
        + ", height="
        + height
        + '}';
  }
}
