package com.starfishst.core.utils.cache;

import com.starfishst.core.fallback.Fallback;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * This class acts like a 'Cache' allowing programs to hold objects in memory
 *
 * @author Chevy
 * @version 1.0.0
 */
public class Cache {

  /** The timer to run the cache */
  @NotNull private static final Timer timer = new Timer();
  /** The cache */
  @NotNull private static final List<Catchable> cache = new ArrayList<>();
  /** The timer task that runs the cache */
  @NotNull private static TimerTask task = new CacheTask();

  static {
    timer.schedule(task, 0, 1000);
  }

  /**
   * Adds an object inside the cache
   *
   * @param catchable the object to put inside the cache
   */
  public static void add(@NotNull Catchable catchable) {
    cache.add(catchable);
  }

  /**
   * Removes an object from the cache
   *
   * @param catchable the object to remove
   */
  public static void remove(@NotNull Catchable catchable) {
    cache.remove(catchable);
  }

  /**
   * Check if the cache contains an object
   *
   * @param catchable to check
   * @return true if the cache contains it
   */
  public static boolean contains(@NotNull Catchable catchable) {
    return cache.contains(catchable);
  }

  /**
   * Get the stream of catchables
   *
   * @return the stream
   */
  @NotNull
  public static Stream<Catchable> stream() {
    return cache.stream();
  }

  /**
   * Get the list of items so called 'cache'
   *
   * @return the list of items
   */
  @NotNull
  public static List<Catchable> getCache() {
    return cache;
  }

  /** Cancels the task that runs the cache */
  public static void cancelTask() {
    task.cancel();
  }

  /**
   * Get the task that the cache needs to run
   *
   * @return the task that the cache uses to run
   */
  @NotNull
  public static TimerTask getTask() {
    return task;
  }

  /** The task that run every second until the cache unloads */
  static class CacheTask extends TimerTask {

    @Override
    public void run() {
      cache.forEach(
          catchable -> {
            catchable.reduceTime();
            if (catchable.getSecondsLeft() > 0) {
              catchable.onSecondsPassed();
            } else {
              try {
                catchable.onRemove();
              } catch (ConcurrentModificationException e) {
                Fallback.addError(
                    catchable
                        + " caused a "
                        + ConcurrentModificationException.class
                        + " please check your Catchable#onRemove()");
                e.printStackTrace();
              }
            }
          });

      cache.removeIf(catchable -> catchable.getSecondsLeft() <= 0);
    }
  }
}
