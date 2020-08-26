package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.RandomUtils;
import com.starfishst.core.utils.math.geometry.containers.Points;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An object that represents a 3 dimensional shape */
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
    return this.getPointsInside().contains(point);
  }

  /**
   * Check in another shape is inside this one
   *
   * @param another the other shape to check if it is inside this one
   * @return true if it is inside this shape
   */
  default boolean contains(@NotNull Shape another) {
    return another.getPointsInside().size() == this.intersectingPoints(another).size();
  }

  /**
   * Check if another shape intersects with this one
   *
   * @param another the other shape to check
   * @return true if part of it is inside this shape
   */
  default boolean intersects(@NotNull Shape another) {
    return !this.intersectingPoints(another).isEmpty();
  }

  /**
   * Get where the shapes are intersecting
   *
   * @param another the shape to check where it is intersecting
   * @return the points where this shapes are intersecting
   */
  @NotNull
  default Points intersectingPoints(@NotNull Shape another) {
    Set<Point> points = new HashSet<>();
    for (Point point : another.getPointsInside().getPoints()) {
      if (this.contains(point)) {
        points.add(point);
      }
    }
    return new Points(points);
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
  Points getPointsInside();

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
  default Points getFacePoints() {
    Points pointsInside = getPointsInside();
    if (pointsInside.isInfinite()) {
      throw new UnsupportedOperationException(
          "There's infinite points. This operation would never end");
    } else {
      Set<Point> points = new HashSet<>();
      for (Point point : this.getPointsInside().getPoints()) {
        if (this.isFacePoint(point)) {
          points.add(point);
        }
      }
      return new Points(points);
    }
  }

  /**
   * Get a random point inside of the shape
   *
   * @return the random point
   */
  @NotNull
  default Point getRandomPoint() {
    return RandomUtils.getRandom(getPointsInside().getPoints());
  }

  /**
   * Get the volume of the shape
   *
   * @return the volume of the shape
   */
  double getVolume();
}
