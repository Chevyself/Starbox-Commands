package com.starfishst.core.utils;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An atomic that can be a null */
public class NullableAtomic<O> {

  /** The object of the class that is stored in the atomic */
  @Nullable private O o;

  /**
   * Create an instance
   *
   * @param o the initial object of the atomic
   */
  public NullableAtomic(@Nullable O o) {
    this.o = o;
  }

  /** Create an instance */
  public NullableAtomic() {
    this.o = null;
  }

  /**
   * Get the object inside the atomic
   *
   * @return the object inside the atomic
   */
  @Nullable
  public O get() {
    return o;
  }

  /**
   * Get the object or return a default not null one
   *
   * @param notNull the not null object
   * @return the object or default one
   */
  @NotNull
  public O getOr(@NotNull O notNull) {
    return o == null ? notNull : o;
  }

  /**
   * Get the object or return a default not null one
   *
   * @param supplier the supplier of the non null object
   * @return the object or default one with the supplier
   */
  @NotNull
  public O getOr(@NotNull Supplier<O> supplier) {
    return o == null ? supplier.get() : o;
  }

  /**
   * Set the object inside the atomic
   *
   * @param o the new object to be in the atomic
   */
  public void set(@Nullable O o) {
    this.o = o;
  }
}
