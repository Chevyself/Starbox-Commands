package com.starfishst.core.utils.math.geometry;

import com.starfishst.core.utils.math.MathUtils;
import org.jetbrains.annotations.NotNull;

/** A single point in a shape */
public class Point {

  /** The x position of the point */
  private double x;
  /** The y position of the point */
  private double y;
  /** The z position of the point */
  private double z;
  /**
   * Create the point shape
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
   * @return a new instance of this point
   */
  @NotNull
  public Point duplicate() {
    return new Point(x, y, z);
  }

  /**
   * Get the distance between two points
   *
   * @param another the another point to check the distance
   * @return the distance between the two
   */
  public double distance(@NotNull Point another) {
    return Math.sqrt(
        MathUtils.square(another.getX() - x)
            + MathUtils.square(another.getY() - y)
            + MathUtils.square(another.getZ() - z));
  }

  /**
   * Sums this point with another
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

  public double size() {
    return x + y + z;
  }

  /**
   * Subtracts this points with another
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
   * Checks if this point is smaller than another point
   *
   * @param point the point to check if it is bigger than this one
   * @return true if this point is smaller than the queried one
   */
  public boolean lowerThan(@NotNull Point point) {
    return size() < point.size();
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

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", z=" + z;
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
