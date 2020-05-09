package com.starfishst.core.utils.cache;

import com.starfishst.core.fallback.Fallback;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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

  static {
    timer.schedule(new CacheTask(), 0, 1000);
  }

  /**
   * Adds an object inside the cache
   *
   * @param catchable the object to put inside the cache
   */
  public static void addToCache(@NotNull Catchable catchable) {
    cache.add(catchable);
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
