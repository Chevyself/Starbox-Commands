package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.providers.type.BungeeExtraArgumentProvider;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/** Provides the command manager the sender of the command */
public class CommandSenderProvider implements BungeeExtraArgumentProvider<CommandSender> {

  @NotNull
  @Override
  public CommandSender getObject(@NotNull CommandContext context) {
    return context.getSender();
  }

  @Override
  public @NotNull Class<CommandSender> getClazz() {
    return CommandSender.class;
  }
}
