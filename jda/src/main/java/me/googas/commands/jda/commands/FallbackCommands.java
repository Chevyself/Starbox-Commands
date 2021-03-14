package me.googas.commands.jda.commands;

import java.util.LinkedHashMap;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.annotations.Parent;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.jda.utils.embeds.EmbedFactory;
import me.googas.starbox.fallback.Fallback;

/** The commands related to fallback */
public class FallbackCommands {

  @NonNull @Getter @Setter private Fallback fallback;

  /**
   * Create the fallback commands
   *
   * @param fallback the fallback that will be managed by these commands
   */
  public FallbackCommands(@NonNull Fallback fallback) {
    this.fallback = fallback;
  }

  @Command(aliases = "errors", description = "See what errors does the Command Manager have")
  @Parent
  public Result error() {
    List<String> list = fallback.getErrors();
    if (list.isEmpty()) {
      return new Result(ResultType.GENERIC, "There's no errors :)");
    } else {
      LinkedHashMap<String, String> errors = new LinkedHashMap<>();
      for (int i = 0; i < list.size(); i++) {
        errors.put(String.valueOf(i), list.get(i));
      }
      return new Result(
          ResultType.GENERIC,
          EmbedFactory.newEmbed(
                  "Errors", "This is a list of errors", null, null, null, null, errors, true)
              .getAsMessageQuery()
              .build());
    }
  }

  /**
   * Clear the list of errors
   *
   * @return a success message saying that errors were cleared
   */
  @Command(aliases = "clear", description = "Clear the list of errors")
  public Result clear() {
    fallback.getErrors().clear();
    return new Result("Errors cleared");
  }
}
