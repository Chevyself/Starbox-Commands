package com.starfishst.core.utils.math.geometry.modifier;

import com.starfishst.core.utils.Atomic;
import com.starfishst.core.utils.math.geometry.Point;
import com.starfishst.core.utils.math.geometry.Shape;
import com.starfishst.core.utils.math.geometry.containers.Points;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Returns the area where the regions intersect */
public class Intersect implements Modifier {

  /** The id of the shape */
  @Nullable private final String id;
  /** The shapes to calculate where they intersect */
  private final Set<Shape> shapes;

  /**
   * Create the intersect modifier
   *
   * @param id the id of the shape
   * @param shapes the shapes to get the area where they intersect
   */
  public Intersect(@Nullable String id, Set<Shape> shapes) {
    this.id = id;
    this.shapes = shapes;
  }

  /**
   * The id to identify a shape in runtime
   *
   * @return the id
   */
  @Override
  public @Nullable String getId() {
    return id;
  }

  /**
   * Get all the points inside the shape
   *
   * @return the points inside
   */
  @Override
  public @NotNull Points getPointsInside() {
    Atomic<Points> atomic = new Atomic<>(new Points(new HashSet<>()));
    shapes.forEach(
        shape -> {
          shapes.forEach(
              compare -> {
                compare
                    .getPointsInside()
                    .forEach(
                        point -> {
                          if (shape.getPointsInside().contains(point)) {
                            atomic.get().add(point);
                          }
                        });
              });
        });
    return atomic.get();
  }

  /**
   * Get the minimum point of the shape
   *
   * @return the minimum point of the shape
   */
  @Override
  public @NotNull Point getMinimum() {
    return getPointsInside().getMinimum();
  }

  /**
   * Get the maximum point of the shape
   *
   * @return the maximum point of the shape
   */
  @Override
  public @NotNull Point getMaximum() {
    return getPointsInside().getMaximum();
  }

  /**
   * Get the volume of the shape
   *
   * @return the volume of the shape
   */
  @Override
  public double getVolume() {
    return 0;
  }

  @Override
  public Collection<Shape> getShapes() {
    return shapes;
  }
}
