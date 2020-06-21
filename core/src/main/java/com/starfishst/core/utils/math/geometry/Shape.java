package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A geometry shape. */
public interface Shape {

  /**
   * Whether a point is a face point or not
   *
   * @param point the point to check if it is a face point
   * @return true if it is
   */
  default boolean isFacePoint(@NotNull Point point) {
    Point x = new Point(1, 0, 0);
    Point y = new Point(0, 1, 0);
    Point z = new Point(0, 0, 1);
    return !contains(point.sum(x))
        || !contains(point.subtract(x))
        || !contains(point.sum(y))
        || !contains(point.subtract(y))
        || !contains(point.sum(z))
        || !contains(point.subtract(z));
  }

  /**
   * Check if this shape contains a point inside of it
   *
   * @param point the point to check if it is inside this shape
   * @return true if it is inside
   */
  default boolean contains(@NotNull Point point) {
    return getPointsInside().contains(point);
  }

  /**
   * Check in another shape is inside this one
   *
   * @param another the other shape to check if it is inside this one
   * @return true if it is inside this shape
   */
  default boolean contains(@NotNull Shape another) {
    return another.getPointsInside().size() == intersectingPoints(another).size();
  }

  /**
   * Check if another shape intersects with this one
   *
   * @param another the other shape to check
   * @return true if part of it is inside this shape
   */
  default boolean intersects(@NotNull Shape another) {
    return !intersectingPoints(another).isEmpty();
  }

  /**
   * Get where the shapes are intersecting
   *
   * @param another the shape to check where it is intersecting
   * @return the points where this shapes are intersecting
   */
  @NotNull
  default List<Point> intersectingPoints(@NotNull Shape another) {
    List<Point> points = new ArrayList<>();
    for (Point point : another.getPointsInside()) {
      if (this.contains(point)) {
        points.add(point);
      }
    }
    return points;
  }

  /**
   * The id to identify a shape in runtime
   *
   * @return the id
   */
  @Nullable
  String getId();

  /**
   * Get all the points inside the shape
   *
   * @return the points inside
   */
  @NotNull
  List<Point> getPointsInside();

  /**
   * Get the minimum point of the shape
   *
   * @return the minimum point of the shape
   */
  @NotNull
  Point getMinimum();

  /**
   * Get the maximum point of the shape
   *
   * @return the maximum point of the shape
   */
  @NotNull
  Point getMaximum();

  /**
   * Get the points that are the 'faces' of the shape, this means that the point above, below, left,
   * right, in front or behind is not a point inside the shape
   *
   * @return the points that are the faces of the shape
   */
  @NotNull
  default List<Point> getFacePoints() {
    return getPointsInside().stream().filter(this::isFacePoint).collect(Collectors.toList());
  }

  /**
   * Get a random point inside of the shape
   *
   * @return the random point
   */
  @NotNull
  default Point getRandomPoint() {
    return RandomUtils.getRandom(getPointsInside());
  }

  /**
   * Get the volume of the shape
   *
   * @return the volume of the shape
   */
  double getVolume();
}
