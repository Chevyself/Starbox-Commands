package me.googas;

import me.googas.commands.annotations.Free;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Required;
import me.googas.commands.system.Command;
import me.googas.commands.system.Result;
import me.googas.commands.time.annotations.TimeAmount;

@SuppressWarnings("JavaDoc")
public class Commands {

  @Command(aliases = "a")
  public Result a(@Required String b, @Required @Multiple String c) {
    return new Result("b = " + b + ", c = " + c);
  }

  @Command(aliases = "message")
  public Result message(@Free @Multiple String message) {
    return new Result(message == null || message.isEmpty() ? "No message was sent" : message);
  }

  @Command(aliases = "hello", cooldown = @TimeAmount(value = "10s"))
  public Result hello() {
    return new Result("World").setApplyCooldown(true);
  }
}
