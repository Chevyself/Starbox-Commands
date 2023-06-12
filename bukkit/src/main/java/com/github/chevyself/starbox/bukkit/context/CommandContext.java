package com.github.chevyself.starbox.bukkit.context;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;

/** The context of a Bukkit command. */
public class CommandContext implements StarboxCommandContext<CommandContext, BukkitCommand> {

  @NonNull @Getter private final CommandLineParser commandLineParser;
  @NonNull @Getter private final BukkitCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider<CommandContext> messagesProvider;

  /**
   * Create a Bukkit context.
   *
   * @param parser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the sender that executed the command
   * @param string the input strings joined
   * @param strings the strings from the command execution
   * @param providersRegistry the registry for the command context to use
   * @param messagesProvider the messages' provider used in this context
   * @param flags the flags in the input of the command
   */
  public CommandContext(
      @NonNull CommandLineParser parser,
      @NonNull BukkitCommand command,
      @NonNull CommandSender sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider) {
    this.commandLineParser = parser;
    this.command = command;
    this.sender = sender;
    this.messagesProvider = messagesProvider;
    this.providersRegistry = providersRegistry;
  }

  @Override
  public @NonNull CommandContext getChildren(@NonNull BukkitCommand subcommand) {
    return new CommandContext(
        this.commandLineParser.copyFrom(1, command.getOptions()),
        subcommand,
        this.sender,
        this.providersRegistry,
        this.messagesProvider);
  }
}
