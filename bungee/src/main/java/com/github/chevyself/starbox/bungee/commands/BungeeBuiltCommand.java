package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import lombok.Getter;
import lombok.NonNull;

public class BungeeBuiltCommand extends AbstractBuiltCommand<CommandContext, BungeeCommand>
    implements BungeeCommand {

  @NonNull @Getter private final BungeeAdapter adapter;
  @NonNull @Getter private final BungeeCommandExecutor executor;
  @Getter private final String permission;

  public BungeeBuiltCommand(
      @NonNull CommandBuilder<CommandContext, BungeeCommand> builder,
      @NonNull BungeeAdapter adapter) {
    super(builder);
    CommandMetadata metadata = builder.getMetadata();
    this.adapter = adapter;
    this.executor =
        new BungeeCommandExecutor(this, metadata.has("async") ? metadata.get("async") : false);
    this.permission = metadata.has("permission") ? metadata.get("permission") : null;
  }
}
