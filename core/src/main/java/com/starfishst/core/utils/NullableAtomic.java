package com.starfishst.core.utils;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Atomic} but that can have a null value.
 *
 * @param <O> the type of object inside this atomic
 */
public class NullableAtomic<O> {

  /** The object of the class that is stored in the atomic */
  @Nullable private O o;

  /**
   * Create an instance of the atomic object
   *
   * @param o the initial object of the atomic
   */
  public NullableAtomic(@Nullable O o) {
    this.o = o;
  }

  /** Create an instance of the atomic object */
  public NullableAtomic() {
    this.o = null;
  }

  /**
   * Get the object inside this atomic instance.
   *
   * @return the object inside this atomic instance.
   */
  @Nullable
  public O get() {
    return o;
  }

  /**
   * Get the object or return a default not null one. A simple check to get a not null object
   *
   * @param notNull the not null object to provide in case the {@link #o} is null
   * @return the object or the default one if {@link #o} is null
   */
  @NotNull
  public O getOr(@NotNull O notNull) {
    return o == null ? notNull : o;
  }

  /**
   * Get the object or return a default not null one but using a {@link Supplier} of the object. A
   * simple check to get a not null object
   *
   * @param supplier the supplier of the non null object in case {@link #o} is null
   * @return the object or the default one from the supplier if {@link #o} is null
   */
  @NotNull
  public O getOr(@NotNull Supplier<O> supplier) {
    return o == null ? supplier.get() : o;
  }

  /**
   * Set the object inside this atomic instance
   *
   * @param o the new object to set in the atomic instance
   */
  public void set(@Nullable O o) {
    this.o = o;
  }
}
