package com.starfishst.events;

import com.starfishst.utils.events.ListenPriority;
import com.starfishst.utils.events.Listener;
import org.jetbrains.annotations.NotNull;

public class TestListener {

  @Listener(priority = ListenPriority.LOWEST)
  public void onTestEvent(@NotNull TestEvent event) {
    System.out.println(event.getMessage());
  }

  @Listener(priority = ListenPriority.HIGHEST)
  public void onTestEventHigh(@NotNull TestEvent event) {
    System.out.println("Called with highest priority " + event.getMessage());
  }

  @Listener(priority = ListenPriority.LOWEST)
  public void onTestCancellable(@NotNull TestCancellableEvent event) {
    event.setCancelled(true);
  }

  @Listener(priority = -1)
  public void onTestCancellableListen(@NotNull TestCancellableEvent event) {
    System.out.println("Event is cancelled? " + event.isCancelled());
  }
}
