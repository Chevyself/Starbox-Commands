package me.googas.commands.bungee.context;

import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.util.Strings;
import net.md_5.bungee.api.CommandSender;

/** The context for bungee commands. */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter @Delegate private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create an instance.
   *
   * @param sender the sender of the command
   * @param strings the command line in separated strings
   * @param messagesProvider the messages provider for this context
   * @param registry the registry used in the context
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
