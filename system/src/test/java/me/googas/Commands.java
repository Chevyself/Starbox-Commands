package me.googas;

import me.googas.commands.annotations.Free;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Required;
import me.googas.commands.arguments.ArgumentBehaviour;
import me.googas.commands.flags.Flag;
import me.googas.commands.system.Command;
import me.googas.commands.system.Result;
import me.googas.commands.system.SystemResult;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.time.annotations.TimeAmount;

@SuppressWarnings("JavaDoc")
public class Commands {

  @Command(
      aliases = "a",
      options = {@Flag(aliases = "message", value = "Hello world!")})
  public SystemResult a(
      CommandContext context,
      @Required(name = "b", behaviour = ArgumentBehaviour.MULTIPLE) String b,
      @Required(name = "c", behaviour = ArgumentBehaviour.MULTIPLE) String c,
      @Required(name = "d") int number,
      @Required(name = "e") int number2,
      @Free(behaviour = ArgumentBehaviour.CONTINUOUS) String extra) {
    return new Result(
        "b = "
            + b
            + ", c = "
            + c
            + ", d = "
            + number
            + ", e = "
            + number2
            + ", message = "
            + context.getFlagValue("message")
            + ", "
            + extra);
  }

  @Command(aliases = "message")
  public SystemResult message(@Free @Multiple String message) {
    return new Result(message == null || message.isEmpty() ? "No message was sent" : message);
  }

  @Command(
      aliases = "hello",
      cooldown = @TimeAmount(value = "10s"),
      options = @Flag(aliases = "message", value = "World!"))
  public SystemResult hello(CommandContext context) {
    return new Result(context.getFlagValue("message").orElse("IDK :(")).setCooldown(true);
  }

  @Command(aliases = "trollface")
  public void trollface(
      @Required(name = "message") String message, @Required(name = "number") int number) {
    for (int i = 0; i < number; i++) {
      System.out.println(message);
    }
  }
}
