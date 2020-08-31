package com.starfishst.core.objects;

/** When a number is required but you also want it to be null */
public class JoinedNumber {

  /** The number as double to get it as any type of primitive */
  private final double number;

  /**
   * Create a custom joined number
   *
   * @param number the number
   */
  public JoinedNumber(double number) {
    this.number = number;
  }

  /**
   * Get the number as int
   *
   * @return the number as int
   */
  public int asInt() {
    return (int) number;
  }

  /**
   * Get the number as long
   *
   * @return the number as long
   */
  public long asLong() {
    return (long) number;
  }

  /**
   * Get the number as double
   *
   * @return the number as double
   */
  public double asDouble() {
    return number;
  }

  /**
   * Get the number as float
   *
   * @return the number as float
   */
  public float asFloat() {
    return (float) number;
  }
}
