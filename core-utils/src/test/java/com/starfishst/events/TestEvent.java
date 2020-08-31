package com.starfishst.events;

import com.starfishst.utils.events.Event;
import org.jetbrains.annotations.NotNull;

/** A test event */
public class TestEvent implements Event {

  @NotNull
  public String getMessage() {
    return "Hello world!";
  }
}
