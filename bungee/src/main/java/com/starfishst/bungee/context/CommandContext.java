package com.starfishst.bungee.context;

import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.registry.ImplProvidersRegistry;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Strings;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/** The context for bungee commands */
public class CommandContext implements ICommandContext {

  /** The sender of bungee commands */
  @NotNull private final CommandSender sender;
  /** The command line */
  @NotNull private final String string;
  /** The command line in separated strings */
  @NotNull private final String[] strings;
  /** The provider of messages for this context */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param sender the sender of the command
   * @param strings the command line in separated strings
   * @param messagesProvider the messages provider for this context
   */
  public CommandContext(
      @NotNull CommandSender sender,
      @NotNull String[] strings,
      @NotNull MessagesProvider messagesProvider) {
    this.sender = sender;
    this.string = Strings.fromArray(strings);
    this.strings = strings;
    this.messagesProvider = messagesProvider;
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

  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return ImplProvidersRegistry.getInstance();
  }

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
