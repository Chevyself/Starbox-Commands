package me.googas.testing;

import me.googas.commands.annotations.Free;
import me.googas.commands.annotations.Required;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.result.Result;
import me.googas.starbox.Strings;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;

public class TestCommands {

  @Command(aliases = "test", description = "This is a test command")
  public Result test(
      @Required(name = "name", description = "Your name") String name,
      @Required(name = "game", description = "Your favourite game") String game,
      @Free(name = "age", description = "Your age") int age) {
    return Result.builder()
        .setDescription(
            Strings.format(
                "Hello {0}! Your favourite game is {1} and you are {2} years old", name, game, age))
        .build();
  }

  @Command(aliases = "foo", description = "Who is a foo")
  public Result foo(@Required(name = "foo", description = "The foo") Member member) {
    return Result.builder()
        .setMessage(() -> new MessageBuilder().setContent("The foo is ").append(member).build())
        .build();
  }
}
