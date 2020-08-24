package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.math.MathUtils;
import com.starfishst.core.utils.math.geometry.containers.Points;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A sphere */
public class Sphere implements Shape {

  /** The id of the sphere */
  @Nullable private final String id;
  /** The center of the sphere */
  @NotNull private Point center;
  /** The radius of the sphere */
  private double radius;

  /**
   * Create the sphere
   *
   * @param id the id of the sphere
   * @param center the center of the sphere
   * @param radius the radius of the sphere
   */
  public Sphere(@Nullable String id, @NotNull Point center, double radius) {
    this.id = id;
    this.center = center;
    this.radius = radius;
  }

  /**
   * Set the center of the sphere
   *
   * @param center the new center of the sphere
   */
  public void setCenter(@NotNull Point center) {
    this.center = center;
  }

  /**
   * Set the radius of the sphere
   *
   * @param radius the new radius of the sphere
   */
  public void setRadius(double radius) {
    this.radius = radius;
  }

  /**
   * Get the center of the sphere
   *
   * @return the center of the sphere
   */
  public Point getCenter() {
    return center;
  }

  /**
   * Get the radius of the sphere
   *
   * @return the radius of the sphere
   */
  public double getRadius() {
    return radius;
  }

  @Override
  public @Nullable String getId() {
    return this.id;
  }

  @Override
  public boolean contains(@NotNull Point point) {
    return (MathUtils.square(point.getX() - center.getX())
            + MathUtils.square(point.getY() - center.getY())
            + MathUtils.square(point.getZ() - center.getZ())
        <= MathUtils.square(radius));
  }

  @Override
  public double getVolume() {
    return 4 / 3f * Math.PI * MathUtils.square(radius);
  }

  @Override
  public @NotNull Points getPointsInside() {
    return new Points(
        new Box(getMinimum(), getMaximum(), null)
            .getPointsInside().stream().filter(this::contains).collect(Collectors.toSet()));
  }

  @Override
  public @NotNull Point getMinimum() {
    return new Point(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
  }

  @Override
  public @NotNull Point getMaximum() {
    return new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius);
  }

  @Override
  public String toString() {
    return "Sphere{" + "id='" + id + '\'' + ", center=" + center + ", radius=" + radius + '}';
  }
}
