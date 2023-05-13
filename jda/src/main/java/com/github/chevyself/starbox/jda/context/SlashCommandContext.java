package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.jda.JdaCommand;
import com.github.chevyself.starbox.jda.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Represents the context of a command executed via {@link
 * net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent}.
 */
public class SlashCommandContext implements CommandContext {

  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final JdaCommand command;
  @NonNull @Getter private final User sender;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<FlagArgument> flags;
  @NonNull @Getter private final SlashCommandInteractionEvent event;
  @NonNull @Getter private final List<OptionMapping> options;
  @NonNull private final MessageChannel channel;

  /**
   * Create the context.
   *
   * @param jda the jda instance of the command manager
   * @param command the command for which this context was created
   * @param sender the user that executed the command
   * @param string the input strings joined
   * @param strings the strings representing the options of the command
   * @param registry the registry for the objects
   * @param messagesProvider the provider for messages
   * @param flags the flags in the input of the command
   * @param event the event that executed the command
   * @param options the options that are executing the command
   * @param channel the channel where the command was executed
   */
  public SlashCommandContext(
      @NonNull JDA jda,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull String string,
      @NonNull String[] strings,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags,
      @NonNull SlashCommandInteractionEvent event,
      @NonNull List<OptionMapping> options,
      @NonNull MessageChannel channel) {
    this.jda = jda;
    this.command = command;
    this.sender = sender;
    this.string = string;
    this.strings = strings;
    this.registry = registry;
    this.messagesProvider = messagesProvider;
    this.flags = flags;
    this.event = event;
    this.options = options;
    this.channel = channel;
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
  public @NonNull SlashCommandContext getChildren() {
    String[] copy = Arrays.copyOfRange(strings, 1, strings.length);
    FlagArgument.Parser parse = FlagArgument.parse(command.getOptions(), copy);
    return new SlashCommandContext(
        this.jda,
        command,
        this.sender,
        parse.getArgumentsString(),
        parse.getArgumentsArray(),
        this.registry,
        this.messagesProvider,
        parse.getFlags(),
        event,
        this.options,
        this.channel);
  }
}
