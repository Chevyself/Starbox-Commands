package com.starfishst.core.utils.math.geometry.containers;

import com.starfishst.core.utils.math.geometry.Point;
import org.jetbrains.annotations.NotNull;

/** An infinite point */
public class InfinitePoint extends Point {

  /** Get whether the point goes to infinite negative or infinite positive */
  private final boolean negative;

  /**
   * Create the point shape
   *
   * @param negative whether the infinite point goes towards infinite negative or maximum negative
   */
  public InfinitePoint(boolean negative) {
    super(
        negative ? Double.MIN_VALUE : Double.MAX_VALUE,
        negative ? Double.MIN_VALUE : Double.MAX_VALUE,
        negative ? Double.MIN_VALUE : Double.MAX_VALUE);
    this.negative = negative;
  }

  /**
   * Creates a new instance of this same point
   *
   * @return a new instance of this point
   */
  @Override
  public @NotNull Point duplicate() {
    return new InfinitePoint(negative);
  }

  /**
   * Get the distance between two points
   *
   * @param another the another point to check the distance
   * @return the distance between the two
   */
  @Override
  public double distance(@NotNull Point another) {
    return Double.MAX_VALUE;
  }

  /**
   * Sums this point with another
   *
   * @param point the other point to sum
   * @return the sum of the two points
   */
  @Override
  public @NotNull Point sum(@NotNull Point point) {
    return duplicate();
  }

  /**
   * Get the size of the point. The size if the sum of all the coordinates
   *
   * @return the size of the point
   */
  @Override
  public double size() {
    return Double.MAX_VALUE;
  }

  /**
   * Subtracts this points with another
   *
   * @param point the other point to subtract
   * @return the subtraction of the two points
   */
  @Override
  public @NotNull Point subtract(@NotNull Point point) {
    return duplicate();
  }

  /**
   * Checks if this point is smaller than another point
   *
   * @param point the point to check if it is bigger than this one
   * @return true if this point is smaller than the queried one
   */
  @Override
  public boolean lowerThan(@NotNull Point point) {
    return negative;
  }

  /**
   * Checks if this point is bigger than another point
   *
   * @param point the point to check if it is smaller than this one
   * @return true if this point is bigger than the queried one
   */
  @Override
  public boolean greaterThan(@NotNull Point point) {
    return !negative;
  }

  /**
   * Set the position x
   *
   * @param x the new position x
   */
  @Override
  public void setX(double x) {
    super.setX(x);
  }

  /**
   * Set the position y
   *
   * @param y the new position y
   */
  @Override
  public void setY(double y) {
    super.setY(y);
  }

  /**
   * Set the position z
   *
   * @param z the new position z
   */
  @Override
  public void setZ(double z) {
    super.setZ(z);
  }

  /**
   * Get the position x
   *
   * @return the position x
   */
  @Override
  public double getX() {
    return super.getX();
  }

  /**
   * Get the position y
   *
   * @return the position y
   */
  @Override
  public double getY() {
    return super.getY();
  }

  /**
   * Get the position z
   *
   * @return the position z
   */
  @Override
  public double getZ() {
    return super.getZ();
  }

  @Override
  public String toString() {
    return negative ? "x=-oo, y=-oo, z=-oo" : "x=oo, y=oo, z=oo";
  }
}
