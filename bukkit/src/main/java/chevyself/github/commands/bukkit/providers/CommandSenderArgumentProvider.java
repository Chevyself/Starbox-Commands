package chevyself.github.commands.bukkit.providers;

import chevyself.github.commands.bukkit.CommandManager;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
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
