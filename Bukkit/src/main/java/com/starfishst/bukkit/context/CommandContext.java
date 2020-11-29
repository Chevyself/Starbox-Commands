package com.starfishst.bukkit.context;

import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import lombok.NonNull;
import me.googas.commons.Lots;
import me.googas.commons.Strings;
import org.bukkit.command.CommandSender;

/** The context of a bukkit command */
public class CommandContext implements ICommandContext {

  /** The sender of the command */
  @NonNull private final CommandSender sender;
  /** The command line that the sender executed */
  @NonNull private final String string;
  /** The command line slitted */
  @NonNull private final String[] strings;
  /** The messages provider of this context */
  @NonNull private final MessagesProvider messagesProvider;
  /** The registry for the command context to use */
  @NonNull private final ProvidersRegistry<CommandContext> registry;

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
    return Lots.arrayFrom(position, this.strings);
  }
}
