package me.googas.testing;

import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.jda.annotations.Command;
import com.github.chevyself.starbox.jda.annotations.Entry;
import com.github.chevyself.starbox.jda.result.Result;
import com.github.chevyself.starbox.time.annotations.TimeAmount;
import com.github.chevyself.starbox.util.Strings;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class TestCommands {

  @Command(
      aliases = "test",
      description = "This is a test command",
      cooldown = @TimeAmount("10s"),
      map = {@Entry(key = "excluded", value = "true")})
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
        .setMessage(
            () -> new MessageCreateBuilder().setContent("The foo is ").mention(member).build())
        .setCooldown(true)
        .build();
  }
}
