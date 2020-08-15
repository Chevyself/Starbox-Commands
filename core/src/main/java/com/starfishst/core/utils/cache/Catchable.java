package com.starfishst.core.utils.cache;

import com.starfishst.core.utils.time.Time;
import com.starfishst.core.utils.time.Unit;
import org.jetbrains.annotations.NotNull;

/** This is an object that can be put inside the cache */
public abstract class Catchable {

  /** The time to remove the object from the cache */
  @NotNull private final transient Time toRemove;
  /** The seconds left of the object inside the cache */
  private transient long secondsLeft;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the object from the cache
   */
  public Catchable(@NotNull Time toRemove) {
    this.toRemove = toRemove;
    addToCache();
  }

  /** Adds this object to the cache */
  public void addToCache() {
    Cache.add(refresh());
  }

  /**
   * Refreshes the time staying inside the cache
   *
   * @return for convenience return this same object
   */
  public Catchable refresh() {
    secondsLeft = this.toRemove.getAs(Unit.SECONDS).getValue();
    return this;
  }

  /** Reduces the seconds left */
  public void reduceTime() {
    secondsLeft--;
  }

  /** Unloads this from the cache */
  public void unload() {
    Cache.remove(this);
  }

  /** When seconds have passed inside the cache this method will be called */
  public abstract void onSecondsPassed();

  /** When the seconds left is less than 0 and the object will be removed this will be called */
  public abstract void onRemove();

  /**
   * Get the seconds left of the object inside the cache
   *
   * @return the seconds left of the object inside the cache
   */
  public long getSecondsLeft() {
    return secondsLeft;
  }

  /**
   * Get the seconds left but as a time object for different usages
   *
   * @return the seconds left as time
   */
  @NotNull
  public Time getTimeLeft() {
    return new Time(secondsLeft, Unit.SECONDS);
  }
}
