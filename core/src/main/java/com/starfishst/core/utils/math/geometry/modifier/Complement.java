package com.starfishst.core.utils.math.geometry.modifier;

import com.starfishst.core.utils.math.geometry.Point;
import com.starfishst.core.utils.math.geometry.Shape;
import com.starfishst.core.utils.math.geometry.containers.Points;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Subtracts the intersecting points for the first shape */
public class Complement implements Modifier {

  /** The id of the shape */
  @Nullable private final String id;
  /** The shape to complement */
  @NotNull private final Shape shape;
  /** The shapes inside the union */
  private final Set<Shape> shapes;

  public Complement(@Nullable String id, @NotNull Shape shape, Set<Shape> shapes) {
    this.id = id;
    this.shape = shape;
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
    Points points = new Points(new HashSet<>());
    shapes.forEach(
        shape -> {
          if (!shape.getPointsInside().isInfinite()) {
            shape
                .getPointsInside()
                .forEach(
                    point -> {
                      if (!this.shape.contains(point)) {
                        points.add(point);
                      }
                    });
          }
        });
    return points;
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
    Set<Shape> shapes = new HashSet<>(this.shapes);
    shapes.add(shape);
    return shapes;
  }
}
