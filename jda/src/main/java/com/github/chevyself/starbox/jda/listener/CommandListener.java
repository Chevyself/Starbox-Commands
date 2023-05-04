package com.github.chevyself.starbox.jda.listener;

import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.jda.CommandManager;
import com.github.chevyself.starbox.jda.JdaCommand;
import com.github.chevyself.starbox.jda.ListenerOptions;
import com.github.chevyself.starbox.jda.UnknownCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GenericCommandContext;
import com.github.chevyself.starbox.jda.context.GuildCommandContext;
import com.github.chevyself.starbox.jda.context.SlashCommandContext;
import com.github.chevyself.starbox.jda.messages.MessagesProvider;
import com.github.chevyself.starbox.jda.result.JdaResult;
import com.github.chevyself.starbox.jda.result.Result;
import com.github.chevyself.starbox.jda.result.ResultType;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

/** The main listener of command execution. */
public class CommandListener implements EventListener {

  @NonNull @Getter private final CommandManager manager;
  @NonNull @Getter private final ListenerOptions listenerOptions;
  @NonNull @Getter private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param manager the command manager
   * @param listenerOptions the options of the manager
   * @param messagesProvider the provider of messages
   */
  public CommandListener(
      @NonNull CommandManager manager,
      @NonNull ListenerOptions listenerOptions,
      @NonNull MessagesProvider messagesProvider) {
    this.manager = manager;
    this.listenerOptions = listenerOptions;
    this.messagesProvider = messagesProvider;
  }

  /**
   * On the event of a message received.
   *
   * @param event the event of a message received
   */
  @SubscribeEvent
  public void onMessageReceived(@NonNull MessageReceivedEvent event) {
    String[] strings = event.getMessage().getContentRaw().split(" +");
    String prefix = listenerOptions.getPrefix(event.isFromGuild() ? event.getGuild() : null);
    if (strings.length < 1 || !strings[0].startsWith(prefix)) {
      return;
    }
    String name = strings[0].substring(prefix.length());
    this.listenerOptions.preCommand(event, name, strings);
    JdaCommand command = this.getCommand(event.isFromGuild() ? event.getGuild() : null, name);
    GenericCommandContext context = this.getCommandContext(event, strings, command);
    JdaResult result = this.getResult(command, name, context);
    this.listenerOptions.handle(result, context);
  }

  /**
   * Execute a command when the event is received.
   *
   * @param event the event of a slash command being received
   */
  @SubscribeEvent
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    String name = event.getName();
    String[] strings =
        event.getOptions().stream().map(OptionMapping::getAsString).toArray(String[]::new);
    this.listenerOptions.preCommand(event, event.getName(), strings);
    JdaCommand command = this.getCommand(event.isFromGuild() ? event.getGuild() : null, name);
    FlagArgument.Parser parse = FlagArgument.parse(command.getOptions(), false, strings);
    CommandContext context =
        new SlashCommandContext(
            event.getJDA(),
            command,
            event.getUser(),
            parse.getArgumentsString(),
            parse.getArgumentsArray(),
            this.manager.getProvidersRegistry(),
            this.messagesProvider,
            parse.getFlags(),
            event,
            event.getOptions(),
            event.getChannel());
    JdaResult result = this.getResult(command, command.getName(), context);
    this.listenerOptions.handle(result, context);
  }

  @NonNull
  private JdaCommand getCommand(Guild guild, @NonNull String name) {
    JdaCommand command = this.manager.getCommand(guild, name);
    return command == null ? new UnknownCommand(this.manager, name) : command;
  }

  /**
   * Get the result of a command execution.
   *
   * @param command the command
   * @param commandName the name of the command
   * @param context the context of the command
   * @return the result of the command execution
   */
  private JdaResult getResult(
      @NonNull JdaCommand command,
      @Deprecated @NonNull String commandName,
      CommandContext context) {
    if (!(command instanceof UnknownCommand)) {
      return command.execute(context);
    } else {
      return Result.forType(ResultType.ERROR)
          .setDescription(this.messagesProvider.commandNotFound(commandName, context))
          .build();
    }
  }

  /**
   * Get the context where the command was executed.
   *
   * @param event the event where the command was executed from
   * @param strings the strings representing the arguments of the command
   * @param commandName the name of the command
   * @return the context of the command
   */
  @NonNull
  private GenericCommandContext getCommandContext(
      @NonNull MessageReceivedEvent event, @NonNull String[] strings, @NonNull JdaCommand command) {
    strings = Arrays.copyOfRange(strings, 1, strings.length);
    FlagArgument.Parser parse = FlagArgument.parse(command.getOptions(), strings);
    if (event.isFromGuild()) {
      return new GuildCommandContext(
          manager.getJda(),
          command,
          event.getAuthor(),
          parse.getArgumentsString(),
          parse.getArgumentsArray(),
          this.manager.getProvidersRegistry(),
          this.messagesProvider,
          parse.getFlags(),
          event,
          event.getChannel(),
          event.getMessage());
    } else {
      return new GenericCommandContext(
          manager.getJda(),
          command,
          event.getAuthor(),
          parse.getArgumentsString(),
          parse.getArgumentsArray(),
          this.manager.getProvidersRegistry(),
          this.messagesProvider,
          parse.getFlags(),
          event,
          event.getChannel(),
          event.getMessage());
    }
  }

  @Override
  public void onEvent(@NonNull @NotNull final GenericEvent genericEvent) {
    if (genericEvent instanceof MessageReceivedEvent) {
      this.onMessageReceived((MessageReceivedEvent) genericEvent);
    } else if (genericEvent instanceof SlashCommandInteractionEvent) {
      this.onSlashCommand((SlashCommandInteractionEvent) genericEvent);
    }
  }
}
