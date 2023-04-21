package com.github.chevyself.starbox.bungee.context;

import com.github.chevyself.starbox.bungee.BungeeCommand;
import com.github.chevyself.starbox.bungee.messages.MessagesProvider;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import net.md_5.bungee.api.CommandSender;

/** The context for bungee commands. */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final BungeeCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<FlagArgument> flags;

  /**
   * Create an instance.
   *
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param strings the command line in separated strings
   * @param string the input strings joined
   * @param registry the registry used in the context
   * @param messagesProvider the messages' provider for this context
   * @param flags the flags in the input of the command
   */
  public CommandContext(
      @NonNull BungeeCommand command,
      @NonNull CommandSender sender,
      @NonNull String[] strings,
      @NonNull String string,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags) {
    this.command = command;
    this.sender = sender;
    this.strings = strings;
    this.string = string;
    this.registry = registry;
    this.messagesProvider = messagesProvider;
    this.flags = flags;
  }
}
