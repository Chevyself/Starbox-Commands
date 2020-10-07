package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.providers.type.BukkitExtraArgumentProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the {@link com.starfishst.bukkit.CommandManager} with the object of {@link
 * CommandSender}
 */
public class CommandSenderArgumentProvider implements BukkitExtraArgumentProvider<CommandSender> {

  @Override
  public @NotNull Class<CommandSender> getClazz() {
    return CommandSender.class;
  }

  @NotNull
  @Override
  public CommandSender getObject(@NotNull CommandContext context) {
    return context.getSender();
  }
}
