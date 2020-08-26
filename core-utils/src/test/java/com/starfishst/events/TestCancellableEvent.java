package com.starfishst.events;

import com.starfishst.utils.events.Cancellable;
import org.jetbrains.annotations.NotNull;

public class TestCancellableEvent implements Cancellable {

  private boolean cancelled;

  @NotNull
  public String getMessage() {
    return "Hello world! From a cancelled event";
  }

  /**
   * Sets whether the event is cancelled
   *
   * @param bol the new value to set if the event is cancelled
   */
  @Override
  public void setCancelled(boolean bol) {
    this.cancelled = bol;
  }

  /**
   * Get whether the event is cancelled
   *
   * @return true if the event is cancelled
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }
}
