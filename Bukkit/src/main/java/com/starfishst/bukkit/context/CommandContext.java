package com.starfishst.bukkit.context;

import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.registry.ImplProvidersRegistry;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Strings;
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

  /**
   * Create a bukkit context
   *
   * @param sender the sender of the bukkit command
   * @param strings the strings from the command execution
   * @param messagesProvider the messages provider used in this context
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
