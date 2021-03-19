package me.googas.commands.bukkit;

import java.util.List;
import me.googas.commands.EasyCommand;
import me.googas.commands.bukkit.context.CommandContext;
import org.bukkit.command.Command;

/** TODO documentation */
public abstract class BukkitCommand extends Command implements EasyCommand<CommandContext> {
  protected BukkitCommand(String name) {
    super(name);
  }

  protected BukkitCommand(
      String name, String description, String usageMessage, List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }
}
