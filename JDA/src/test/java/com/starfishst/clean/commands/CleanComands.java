package com.starfishst.clean.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.annotations.Required;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

/** Commands for cleaning */
public class CleanComands {

  @Command(aliases = "clean", description = "Clean messages", permission = Permission.ADMINISTRATOR)
  public Result clean(
      TextChannel channel,
      @Required(name = "amount", description = "The amount of messages to clean") int amount) {
    if (amount > 100) {
      return new Result(ResultType.ERROR, "You cannot clean more than 100 messages");
    } else {
      channel
          .getHistory()
          .retrievePast(amount)
          .queue(messages -> messages.forEach(message -> message.delete().queue()));
      return new Result(amount + " of messages were cleared");
    }
  }
}
