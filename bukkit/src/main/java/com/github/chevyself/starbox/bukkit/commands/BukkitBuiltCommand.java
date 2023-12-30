package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;

public class BukkitBuiltCommand extends AbstractBuiltCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull @Getter private final BukkitAdapter adapter;
  @NonNull @Getter private final BukkitCommandExecutor executor;
  @Getter private final String permission;

  public BukkitBuiltCommand(
      @NonNull CommandBuilder<CommandContext, BukkitCommand> builder,
      @NonNull BukkitAdapter adapter) {
    super(builder);
    CommandMetadata metadata = builder.getMetadata();
    this.adapter = adapter;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(),
            metadata.has("description") ? metadata.get("description") : "",
            metadata.has("usage") ? metadata.get("usage") : "",
            metadata.has("aliases") ? metadata.get("aliases") : new ArrayList<>(),
            this,
            metadata.has("async") ? metadata.get("async") : false);
    this.permission = metadata.has("permission") ? metadata.get("permission") : null;
  }
}
