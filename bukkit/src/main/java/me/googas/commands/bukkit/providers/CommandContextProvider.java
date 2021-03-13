package me.googas.commands.bukkit.providers;

import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import lombok.NonNull;

/** Provides the {@link CommandManager} with the object of {@link CommandContext} */
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
