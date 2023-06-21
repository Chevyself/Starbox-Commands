package com.github.chevyself.starbox.velocity.context;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.velocitypowered.api.command.CommandSource;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CommandContext implements StarboxCommandContext<CommandContext, VelocityCommand> {

  @NonNull private final CommandLineParser commandLineParser;
  @NonNull private final VelocityCommand command;
  @NonNull private final CommandSource sender;
  @NonNull private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull private final MessagesProvider<CommandContext> messagesProvider;

  public CommandContext(
      @NonNull CommandLineParser commandLineParser,
      @NonNull VelocityCommand command,
      @NonNull CommandSource sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider) {
    this.commandLineParser = commandLineParser;
    this.command = command;
    this.sender = sender;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull CommandContext getChildren(@NonNull VelocityCommand subcommand) {
    return new CommandContext(
        this.commandLineParser.copyFrom(1, subcommand.getOptions()),
        subcommand,
        this.sender,
        this.providersRegistry,
        this.messagesProvider);
  }
}
