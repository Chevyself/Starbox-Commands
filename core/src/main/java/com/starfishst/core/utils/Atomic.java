package com.starfishst.core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * An object to atomize objects. This means that the object can be used inside a lambda using this
 * instance. This is a not null instance check {@link NullableAtomic} to use nul instances
 *
 * @param <O> the type of object that will be contained in this atomic
 */
public class Atomic<O> {

  /** The object of the class that is stored in the atomic */
  @NotNull private O o;

  /**
   * Create an instance of the atomic object
   *
   * @param o the initial object of the atomic must not be null
   */
  public Atomic(@NotNull O o) {
    this.o = o;
  }

  /**
   * Get the object inside the atomic. This object will not be null
   *
   * @return the object inside the atomic
   */
  @NotNull
  public O get() {
    return o;
  }

  /**
   * Set the object inside the atomic it should not be null as this is a not null instance
   *
   * @param o the new object to store in the atomic
   */
  public void set(@NotNull O o) {
    this.o = o;
  }
}
