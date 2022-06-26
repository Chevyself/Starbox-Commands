package me.googas.testing;

import me.googas.commands.annotations.Free;
import me.googas.commands.annotations.Required;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.result.Result;
import me.googas.commands.time.annotations.TimeAmount;
import me.googas.commands.util.Strings;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;

public class TestCommands {

  @Command(aliases = "test", description = "This is a test command", cooldown = @TimeAmount("10s"))
  public Result test(
      @Required(name = "name", description = "Your name") String name,
      @Required(name = "game", description = "Your favourite game") String game,
      @Free(
              name = "age",
              description = "Your age",
              suggestions = {"0", "1"})
          int age) {
    return Result.builder()
        .setDescription(
            Strings.format(
                "Hello {0}! Your favourite game is {1} and you are {2} years old", name, game, age))
        .setCooldown(true)
        .build();
  }

  @Command(aliases = "lol", description = "The lol command", cooldown = @TimeAmount("10s"))
  public Result lol() {
    return Result.builder().setDescription("LOL!").setCooldown(true).build();
  }

  @Command(aliases = "foo", description = "Who is a foo", cooldown = @TimeAmount("10s"))
  public Result foo(@Required(name = "foo", description = "The foo") Member member) {
    return Result.builder()
        .setMessage(() -> new MessageBuilder().setContent("The foo is ").append(member).build())
        .setCooldown(true)
        .build();
  }
}
