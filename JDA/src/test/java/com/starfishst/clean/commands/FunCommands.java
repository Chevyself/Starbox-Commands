package com.starfishst.clean.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.result.Result;
import com.starfishst.core.annotations.Optional;
import com.starfishst.core.utils.RandomUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

/** Comandos para divertirse */
public class FunCommands {

  @Command(
      aliases = {"pene", "nepe"},
      description = "Te mide el pene o el de alguien mas")
  public Result pene(
      User user,
      @Optional(name = "user", description = "El usuario para ver cuanto le mide") Member another) {
    if (another != null) {
      user = another.getUser();
    }
    int i = RandomUtils.getRandom().nextInt(30);
    return new Result("A " + user.getAsMention() + " le mide " + i + " cm");
  }
}
