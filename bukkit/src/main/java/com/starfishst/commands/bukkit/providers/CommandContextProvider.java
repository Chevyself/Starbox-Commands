package com.starfishst.commands.bukkit.providers;

import com.starfishst.commands.bukkit.CommandManager;
import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
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
