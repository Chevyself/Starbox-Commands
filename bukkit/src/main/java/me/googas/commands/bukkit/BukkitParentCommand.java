package me.googas.commands.bukkit;

import java.util.List;
import me.googas.commands.EasyParentCommand;
import me.googas.commands.bukkit.context.CommandContext;

public abstract class BukkitParentCommand extends BukkitCommand
    implements EasyParentCommand<CommandContext, BukkitCommand> {
  protected BukkitParentCommand(String name) {
    super(name);
  }

  protected BukkitParentCommand(
      String name, String description, String usageMessage, List<String> aliases) {
    super(name, description, usageMessage, aliases);
  }
}
