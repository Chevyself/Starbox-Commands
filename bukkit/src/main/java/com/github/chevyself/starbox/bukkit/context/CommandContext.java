package com.github.chevyself.starbox.bukkit.context;

import com.github.chevyself.starbox.bukkit.StarboxBukkitCommand;
import com.github.chevyself.starbox.bukkit.messages.MessagesProvider;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.CommandLineParser;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;

/** The context of a Bukkit command. */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final CommandLineParser commandLineParser;
  @NonNull @Getter private final StarboxBukkitCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;

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
      @NonNull StarboxBukkitCommand command,
      @NonNull CommandSender sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
    this.commandLineParser = parser;
    this.command = command;
    this.sender = sender;
    this.messagesProvider = messagesProvider;
    this.providersRegistry = providersRegistry;
  }
}
