package chevyself.github.commands.bukkit.providers;

import chevyself.github.commands.bukkit.CommandManager;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;

/** Provides the {@link CommandManager} with the object {@link CommandContext}. */
public class CommandContextProvider implements BukkitExtraArgumentProvider<CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @NonNull
  @Override
  public CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }
}
