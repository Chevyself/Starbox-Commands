package com.github.chevyself.starbox.system.context;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.CommandLineParser;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.MessagesProvider;
import com.github.chevyself.starbox.system.SystemCommand;
import com.github.chevyself.starbox.system.context.sender.CommandSender;
import com.github.chevyself.starbox.system.context.sender.ConsoleCommandSender;
import lombok.Getter;
import lombok.NonNull;

/**
 * This context to execute {@link SystemCommand}. This does not include anything different from
 * {@link StarboxCommandContext} the {@link #sender} is the static instance of {@link
 * ConsoleCommandSender#INSTANCE}
 */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final CommandLineParser commandLineParser;
  @NonNull @Getter private final SystemCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;

  /**
   * Create the command context.
   *
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command for which this context was created
   * @param sender the static instance of {@link ConsoleCommandSender#INSTANCE}
   * @param providersRegistry the registry to parse {@link Object} array to run {@link
   *     SystemCommand}
   * @param messagesProvider the messages provider for messages of providers
   */
  public CommandContext(
      @NonNull CommandLineParser commandLineParser,
      @NonNull SystemCommand command,
      @NonNull CommandSender sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
    this.command = command;
    this.sender = sender;
    this.commandLineParser = commandLineParser;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
  }
}
