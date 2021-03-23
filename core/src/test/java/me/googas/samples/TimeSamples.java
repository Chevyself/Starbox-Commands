package me.googas.samples;

import me.googas.commands.time.Time;
import me.googas.commands.time.formatter.HhMmSsFormatter;
import me.googas.commands.time.unit.Unit;

public class TimeSamples {

  public static void main(String[] args) {
    // Creates a time of two seconds
    Time seconds = Time.of(2, Unit.SECONDS);
    // Creates a half week
    Time halfWeek = Time.ofMillis(907200000, true);

    // Get the amount of time in other unit
    Time day = Time.of(1, Unit.DAYS);
    System.out.println("Output 24: " + day.get(Unit.HOURS));

    // Get the value in millis
    double value = Unit.SECONDS.getMillis(2);
    System.out.println("Output 2000: " + value);
  }
}
