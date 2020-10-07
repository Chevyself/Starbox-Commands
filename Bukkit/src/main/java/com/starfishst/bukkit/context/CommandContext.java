package com.starfishst.bukkit.context;

import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import me.googas.commons.Lots;
import me.googas.commons.Strings;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/** The context of a bukkit command */
public class CommandContext implements ICommandContext {

  /** The sender of the command */
  @NotNull private final CommandSender sender;
  /** The command line that the sender executed */
  @NotNull private final String string;
  /** The command line slitted */
  @NotNull private final String[] strings;
  /** The messages provider of this context */
  @NotNull private final MessagesProvider messagesProvider;
  /** The registry for the command context to use */
  @NotNull private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create a bukkit context
   *
   * @param sender the sender of the bukkit command
   * @param strings the strings from the command execution
   * @param messagesProvider the messages provider used in this context
   * @param registry the registry for the command context to use
   */
  public CommandContext(
      @NotNull CommandSender sender,
      @NotNull String[] strings,
      @NotNull MessagesProvider messagesProvider,
      @NotNull ProvidersRegistry<CommandContext> registry) {
    this.sender = sender;
    this.string = Strings.fromArray(strings);
    this.strings = strings;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
  }

  @NotNull
  @Override
  public CommandSender getSender() {
    return this.sender;
  }

  @NotNull
  @Override
  public String getString() {
    return this.string;
  }

  @NotNull
  @Override
  public String[] getStrings() {
    return this.strings;
  }

  @NotNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NotNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public boolean hasFlag(@NotNull String flag) {
    for (String string : this.strings) {
      if (string.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  @Override
  public String[] getStringsFrom(int position) {
    return Lots.arrayFrom(position, this.strings);
  }
}
