package me.googas.experimental;

import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.experimental.Command;
import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.system.Result;
import com.github.chevyself.starbox.system.SystemResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.util.Locale;

@SuppressWarnings("JavaDoc")
@Command(
    aliases = "test",
    flags = {@Flag(aliases = "message", value = "Hello world!")})
public class Commands {

  @Command(
      aliases = "a",
      flags = {@Flag(aliases = "message", value = "Hello world!")})
  public SystemResult a(
      CommandContext context,
      @Required(name = "b", behaviour = ArgumentBehaviour.CONTINUOUS) String b,
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

  @Command(
      aliases = "message",
      flags = {
        @Flag(
            aliases = {
              "capitalize",
              "c",
            },
            value = "false",
            valuable = false)
      })
  public SystemResult message(
      CommandContext context, @Free(behaviour = ArgumentBehaviour.CONTINUOUS) String message) {
    String string = message == null || message.isEmpty() ? "No message was sent" : message;
    return new Result(context.hasFlag("capitalize") ? string.toUpperCase(Locale.ROOT) : string);
  }

  @Command(aliases = "hello", flags = @Flag(aliases = "message", value = "World!"))
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
