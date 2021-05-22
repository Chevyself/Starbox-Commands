package me.googas.commands.system.context;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.MessagesProvider;
import me.googas.commands.system.context.sender.CommandSender;
import me.googas.starbox.Strings;

/**
 * This context to execute {@link me.googas.commands.system.SystemCommand} that does not include
 * anything different from {@link EasyCommandContext} the {@link #sender} is the static instance of
 * {@link me.googas.commands.system.context.sender.ConsoleCommandSender#INSTANCE}
 */
public class CommandContext implements EasyCommandContext {

  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter final String string;

  /**
   * Create the command context
   *
   * @param sender the static instance of {@link
   *     me.googas.commands.system.context.sender.ConsoleCommandSender#INSTANCE}
   * @param strings the input strings read from the {@link
   *     me.googas.commands.system.CommandListener}
   * @param registry the registry to parse {@link Object} array to run {@link
   *     me.googas.commands.system.SystemCommand}
   * @param messagesProvider the messages provider for messages of providers
   */
  public CommandContext(
      @NonNull CommandSender sender,
      @NonNull String[] strings,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider) {
    this.sender = sender;
    this.strings = strings;
    this.string = Strings.fromArray(strings);
    this.registry = registry;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public boolean hasFlag(@NonNull String flag) {
    for (String string : this.strings) {
      if (string.equalsIgnoreCase(flag)) return true;
    }
    return false;
  }
}
