package com.starfishst.events;

import com.starfishst.utils.events.ListenerManager;

public class Test {

  public static void main(String[] args) {
    ListenerManager listenerManager = new ListenerManager();
    listenerManager.registerListeners(new TestListener());
    listenerManager.call(new TestEvent());
    listenerManager.call(new TestCancellableEvent());
  }
}
