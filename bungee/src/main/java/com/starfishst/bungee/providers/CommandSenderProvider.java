package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/** Provides the command manager the sender of the command */
public class CommandSenderProvider
    implements IExtraArgumentProvider<CommandSender, CommandContext> {

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
