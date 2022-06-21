package me.googas.commands.jda.context;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.util.Strings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Represents the context of a command executed via {@link
 * net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent}.
 */
public class SlashCommandContext implements CommandContext {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull protected final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter protected final SlashCommandInteractionEvent event;
  @NonNull protected final MessagesProvider messagesProvider;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final JDA jda;
  @NonNull private final List<OptionMapping> options;
  @NonNull private final MessageChannel channel;
  @NonNull @Getter private final String commandName;
  @NonNull @Getter private final User sender;

  /**
   * Create the context.
   *
   * @param manager
   * @param strings the strings representing the options of the command
   * @param jda the jda instance of the command manager
   * @param channel the channel where the command was executed
   * @param options the options that are executing the command
   * @param commandName the name of the command
   * @param sender the user that executed the command
   * @param messagesProvider the provider for messages
   * @param registry the registry for the objects
   * @param event the event that executed the command
   */
  public SlashCommandContext(
      @NonNull CommandManager manager,
      @NonNull String[] strings,
      @NonNull JDA jda,
      @NonNull MessageChannel channel,
      @NonNull List<OptionMapping> options,
      @NonNull String commandName,
      @NonNull User sender,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull SlashCommandInteractionEvent event) {
    this.manager = manager;
    this.string = Strings.join(strings);
    this.strings = strings;
    this.jda = jda;
    this.options = options;
    this.channel = channel;
    this.commandName = commandName;
    this.sender = sender;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    this.event = event;
  }

  @Override
  public @NonNull Optional<MessageChannel> getChannel() {
    return Optional.of(channel);
  }

  @Override
  public @NonNull Optional<Message> getMessage() {
    return Optional.empty();
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.registry;
  }

  @Override
  public @NonNull MessagesProvider getMessagesProvider() {
    return this.messagesProvider;
  }

  @Override
  public @NonNull SlashCommandContext getChildren() {
    return new SlashCommandContext(
        manager,
        Arrays.copyOfRange(strings, 1, strings.length),
        this.jda,
        this.channel,
        this.options,
        this.commandName,
        this.sender,
        this.messagesProvider,
        this.registry,
        event);
  }
}
