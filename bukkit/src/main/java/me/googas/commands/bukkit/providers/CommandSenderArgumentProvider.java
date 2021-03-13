package me.googas.commands.bukkit.providers;

import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

/** Provides the {@link CommandManager} with the object of {@link CommandSender} */
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
