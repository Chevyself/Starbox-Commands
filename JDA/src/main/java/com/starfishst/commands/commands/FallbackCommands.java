package com.starfishst.commands.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.commands.utils.embeds.EmbedFactory;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.fallback.Fallback;
import java.util.LinkedHashMap;
import java.util.List;

/** The commands related to fallback */
public class FallbackCommands {

  /**
   * Get the errors that were cached by the fallback
   *
   * @return a message displaying errors
   */
  @Command(aliases = "errors", description = "See what errors does the Command Manager have")
  @Parent
  public Result error() {
    List<String> list = Fallback.getErrors().getList();
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
              .getMessage());
    }
  }

  /**
   * Clear the list of errors
   *
   * @return a success message saying that errors were cleared
   */
  @Command(aliases = "clear", description = "Clear the list of errors")
  public Result clear() {
    Fallback.clear();
    return new Result("Errors cleared");
  }
}
