package com.github.chevyself.starbox.velocity;

import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.common.tab.TabCompleter;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.tab.VelocityReflectTabCompleter;
import com.github.chevyself.starbox.velocity.tab.VelocityTabCompleter;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;

public class VelocityCommandExecutor implements SimpleCommand {

  @NonNull
  private static final VelocityReflectTabCompleter reflectCompleter =
      new VelocityReflectTabCompleter();

  @NonNull private static final VelocityTabCompleter genericCompleter = new VelocityTabCompleter();
  @NonNull private final VelocityCommand command;
  @NonNull private final TabCompleter<CommandContext, VelocityCommand, CommandSource> tabCompleter;

  public VelocityCommandExecutor(@NonNull VelocityCommand command) {
    this.command = command;
    if (command instanceof ReflectCommand) {
      this.tabCompleter = VelocityCommandExecutor.reflectCompleter;
    } else {
      this.tabCompleter = VelocityCommandExecutor.genericCompleter;
    }
  }

  @Override
  public void execute(Invocation invocation) {
    CommandContext context =
        new CommandContext(
            CommandLineParser.parse(command.getOptions(), invocation.arguments()),
            command,
            invocation.source(),
            command.getProvidersRegistry(),
            command.getMessagesProvider());
    if (command.isAsync()) {
      CompletableFuture.runAsync(() -> command.execute(context));
    } else {
      command.execute(context);
    }
  }

  @Override
  public List<String> suggest(Invocation invocation) {
    return this.tabCompleter.tabComplete(
        this.command, invocation.source(), invocation.alias(), invocation.arguments());
  }
}
