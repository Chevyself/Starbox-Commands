package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.math.MathUtils;
import org.jetbrains.annotations.NotNull;

/**
 * An object that represents a point inside the cartesian coordinates system. This means this object
 * is represented by an X, Y and Z value.
 */
public class Point {

  /** The x position of the point */
  private double x;
  /** The y position of the point */
  private double y;
  /** The z position of the point */
  private double z;

  /**
   * Create the point
   *
   * @param x the x position of the point
   * @param y the y position of the point
   * @param z the z position of the point
   */
  public Point(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Creates a new instance of this same point
   *
   * @return the new instance of this point
   */
  @NotNull
  public Point duplicate() {
    return new Point(this.x, this.y, this.z);
  }

  /**
   * Get the distance between two points
   *
   * @param another another point to check the distance
   * @return the distance between the two points
   */
  public double distance(@NotNull Point another) {
    return Math.sqrt(
        MathUtils.square(another.getX() - this.x)
            + MathUtils.square(another.getY() - this.y)
            + MathUtils.square(another.getZ() - this.z));
  }

  /**
   * Sums this point with another.
   *
   * <p>The three values ({@link #x}, {@link #y} and {@link #z}) will be summed for both points.
   *
   * @param point the other point to sum
   * @return the sum of the two points
   */
  @NotNull
  public Point sum(@NotNull Point point) {
    double x = this.x + point.getX();
    double y = this.y + point.getY();
    double z = this.z + point.getZ();
    return new Point(x, y, z);
  }

  /**
   * Get the size of the point. The size if the sum of all the coordinates <br>
   * ({@link #x}, {@link #y} and {@link #z})
   *
   * @return the size of the point
   */
  public double size() {
    return x + y + z;
  }

  /**
   * Subtracts this points with another.
   *
   * <p>The three values ({@link #x}, {@link #y} and {@link #z}) will be subtracted for both points.
   *
   * @param point the other point to subtract
   * @return the subtraction of the two points
   */
  @NotNull
  public Point subtract(@NotNull Point point) {
    double x = this.x - point.getX();
    double y = this.y - point.getY();
    double z = this.z - point.getZ();
    return new Point(x, y, z);
  }

  /**
   * Checks if this point is smaller than another point.
   *
   * @param point the point to check if it is bigger than this one
   * @return true if this point is smaller than the queried one
   */
  public boolean lowerThan(@NotNull Point point) {
    return this.size() < point.size();
  }

  /**
   * Checks if this point is bigger than another point
   *
   * @param point the point to check if it is smaller than this one
   * @return true if this point is bigger than the queried one
   */
  public boolean greaterThan(@NotNull Point point) {
    return size() > point.size();
  }

  /**
   * Set the position x
   *
   * @param x the new position x
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Set the position y
   *
   * @param y the new position y
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Set the position z
   *
   * @param z the new position z
   */
  public void setZ(double z) {
    this.z = z;
  }

  /**
   * Get the position x
   *
   * @return the position x
   */
  public double getX() {
    return x;
  }

  /**
   * Get the position y
   *
   * @return the position y
   */
  public double getY() {
    return y;
  }

  /**
   * Get the position z
   *
   * @return the position z
   */
  public double getZ() {
    return z;
  }

  /** Floors the {@link #x}, {@link #y} and {@link #z} values. */
  public void floor() {
    this.x = Math.floor(this.x);
    this.y = Math.floor(this.y);
    this.z = Math.floor(this.z);
  }

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", z=" + z;
  }

  /**
   * Get whether this point is infinite. If any of {@link #x}, {@link #y} and {@link #z} are {@link
   * Double#NEGATIVE_INFINITY} or {@link Double#POSITIVE_INFINITY} it will be true. Using {@link
   * Double#isInfinite()}
   *
   * @return true if the point is infinite if any of its coordinates
   */
  public boolean isInfinite() {
    return Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(z);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof Point)) return false;

    Point point = (Point) object;

    if (Double.compare(point.x, x) != 0) return false;
    if (Double.compare(point.y, y) != 0) return false;
    return Double.compare(point.z, z) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(z);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
