package com.starfishst.core.utils.math.geometry.containers;

import com.starfishst.core.utils.math.geometry.Point;
import com.starfishst.core.utils.math.geometry.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A shape that is every coordinate */
public class Everywhere implements Shape {
  /**
   * The id to identify a shape in runtime
   *
   * @return the id
   */
  @Override
  public @Nullable String getId() {
    return null;
  }

  /**
   * Get all the points inside the shape
   *
   * @return the points inside
   */
  @Override
  public @NotNull Points getPointsInside() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Get the minimum point of the shape
   *
   * @return the minimum point of the shape
   */
  @Override
  public @NotNull Point getMinimum() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Get the maximum point of the shape
   *
   * @return the maximum point of the shape
   */
  @Override
  public @NotNull Point getMaximum() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Get the volume of the shape
   *
   * @return the volume of the shape
   */
  @Override
  public double getVolume() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Whether a point is a face point or not
   *
   * @param point the point to check if it is a face point
   * @return true if it is
   */
  @Override
  public boolean isFacePoint(@NotNull Point point) {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Check if this shape contains a point inside of it
   *
   * @param point the point to check if it is inside this shape
   * @return true if it is inside
   */
  @Override
  public boolean contains(@NotNull Point point) {
    return true;
  }

  /**
   * Check in another shape is inside this one
   *
   * @param another the other shape to check if it is inside this one
   * @return true if it is inside this shape
   */
  @Override
  public boolean contains(@NotNull Shape another) {
    return true;
  }

  /**
   * Check if another shape intersects with this one
   *
   * @param another the other shape to check
   * @return true if part of it is inside this shape
   */
  @Override
  public boolean intersects(@NotNull Shape another) {
    return true;
  }

  /**
   * Get where the shapes are intersecting
   *
   * @param another the shape to check where it is intersecting
   * @return the points where this shapes are intersecting
   */
  @Override
  public @NotNull Points intersectingPoints(@NotNull Shape another) {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Get the points that are the 'faces' of the shape, this means that the point above, below, left,
   * right, in front or behind is not a point inside the shape
   *
   * @return the points that are the faces of the shape
   */
  @Override
  public @NotNull Points getFacePoints() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }

  /**
   * Get a random point inside of the shape
   *
   * @return the random point
   */
  @Override
  public @NotNull Point getRandomPoint() {
    throw new UnsupportedOperationException("There's infinite points in this shape");
  }
}
