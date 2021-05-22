package me.googas.commands.bungee.context;

import java.util.Arrays;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.Strings;
import net.md_5.bungee.api.CommandSender;

/** The context for bungee commands */
public class CommandContext implements EasyCommandContext {

  @NonNull private final CommandSender sender;
  @NonNull private final String string;
  @NonNull private final String[] strings;
  @NonNull private final MessagesProvider messagesProvider;
  @NonNull @Delegate private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create an instance
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
    return this.registry;
  }

  @NonNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return this.messagesProvider;
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
