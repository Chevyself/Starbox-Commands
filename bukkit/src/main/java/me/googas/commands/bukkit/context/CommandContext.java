package me.googas.commands.bukkit.context;

import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.util.Strings;
import org.bukkit.command.CommandSender;

/** The context of a Bukkit command. */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create a Bukkit context.
   *
   * @param sender the sender of the bukkit command
   * @param strings the strings from the command execution
   * @param messagesProvider the messages provider used in this context
   * @param registry the registry for the command context to use
   */
  public CommandContext(
      @NonNull CommandSender sender,
      @NonNull String[] strings,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    this.sender = sender;
    this.string = Strings.join(strings);
    this.strings = strings;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
  }

  @Override
  public boolean hasFlag(@NonNull String flag) {
    for (String string : this.strings) {
      if (string.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }

  @NonNull
  @Override
  public String[] getStringsFrom(int position) {
    return Arrays.copyOfRange(this.strings, position, this.strings.length);
  }
}
