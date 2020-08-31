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

/** A bunch of shapes */
public class Union implements Modifier {

  /** The id of the shape */
  @Nullable private final String id;
  /** The shapes inside the union */
  private final Set<Shape> shapes;

  /**
   * Create the union
   *
   * @param id the id of the union
   * @param shapes the shapes inside the union
   */
  public Union(@Nullable String id, Set<Shape> shapes) {
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
    Atomic<Points> points = new Atomic<>(new Points(new HashSet<>()));
    shapes.forEach(
        shape -> {
          points.get().addAll(shape.getPointsInside());
        });
    return points.get();
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
    double volume = 0;
    for (Shape shape : shapes) {
      volume += shape.getVolume();
    }
    return volume;
  }

  @Override
  public Collection<Shape> getShapes() {
    return shapes;
  }
}
