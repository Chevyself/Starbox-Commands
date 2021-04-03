package me.googas.commands.bukkit.context;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.utility.Series;
import me.googas.utility.Strings;
import org.bukkit.command.CommandSender;

/** The context of a bukkit command */
public class CommandContext implements EasyCommandContext {

  @NonNull private final CommandSender sender;
  @NonNull private final String string;
  @NonNull private final String[] strings;
  @NonNull private final MessagesProvider messagesProvider;
  @NonNull @Delegate private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create a bukkit context
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
    this.string = Strings.fromArray(strings);
    this.strings = strings;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
  }

  @NonNull
  @Override
  public CommandSender getSender() {
    return this.sender;
  }

  @NonNull
  @Override
  public String getString() {
    return this.string;
  }

  @NonNull
  @Override
  public String[] getStrings() {
    return this.strings;
  }

  @NonNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NonNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
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
    return Series.arrayFrom(position, this.strings);
  }
}
