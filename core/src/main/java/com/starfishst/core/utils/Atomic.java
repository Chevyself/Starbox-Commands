package com.starfishst.core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Use any class as an atomic
 *
 * @param <O> the class to use in the atomic
 */
public class Atomic<O> {

  /** The object of the class that is stored in the atomic */
  @NotNull private O o;

  /**
   * Create an instance
   *
   * @param o the initial object of the atomic
   */
  public Atomic(@NotNull O o) {
    this.o = o;
  }

  /**
   * Get the object inside the atomic
   *
   * @return the object inside the atomic
   */
  @NotNull
  public O get() {
    return o;
  }

  /**
   * Set the object inside the atomic
   *
   * @param o the new object to be in the atomic
   */
  public void set(@NotNull O o) {
    this.o = o;
  }
}
