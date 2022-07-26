package me.googas.commands.bukkit.context;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.providers.registry.ProvidersRegistry;
import org.bukkit.command.CommandSender;

/** The context of a Bukkit command. */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final StarboxBukkitCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<FlagArgument> flags;

  /**
   * Create a Bukkit context.
   *
   * @param command the command for which this context was created
   * @param sender the sender that executed the command
   * @param string the input strings joined
   * @param strings the strings from the command execution
   * @param registry the registry for the command context to use
   * @param messagesProvider the messages' provider used in this context
   * @param flags the flags in the input of the command
   */
  public CommandContext(
      @NonNull StarboxBukkitCommand command,
      @NonNull CommandSender sender,
      @NonNull String string,
      @NonNull String[] strings,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags) {
    this.command = command;
    this.sender = sender;
    this.string = string;
    this.strings = strings;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    this.flags = flags;
  }
}
