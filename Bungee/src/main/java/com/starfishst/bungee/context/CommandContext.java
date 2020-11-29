package com.starfishst.bungee.context;

import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import lombok.NonNull;
import me.googas.commons.Lots;
import me.googas.commons.Strings;
import net.md_5.bungee.api.CommandSender;

/** The context for bungee commands */
public class CommandContext implements ICommandContext {

  /** The sender of bungee commands */
  @NonNull private final CommandSender sender;
  /** The command line */
  @NonNull private final String string;
  /** The command line in separated strings */
  @NonNull private final String[] strings;
  /** The provider of messages for this context */
  @NonNull private final MessagesProvider messagesProvider;

  /** The registry used in the context */
  @NonNull private final ProvidersRegistry<CommandContext> registry;

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
    return Lots.arrayFrom(position, this.strings);
  }
}
