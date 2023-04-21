package com.github.chevyself.starbox.bukkit.providers;

import com.github.chevyself.starbox.bukkit.CommandManager;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

/** Provides the {@link CommandManager} with the object {@link CommandSender}. */
public class CommandSenderArgumentProvider implements BukkitExtraArgumentProvider<CommandSender> {

  @Override
  public @NonNull Class<CommandSender> getClazz() {
    return CommandSender.class;
  }

  @NonNull
  @Override
  public CommandSender getObject(@NonNull CommandContext context) {
    return context.getSender();
  }
}
