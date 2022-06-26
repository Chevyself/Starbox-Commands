package me.googas.commands.jda.listener;

import java.util.Arrays;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.JdaCommand;
import me.googas.commands.jda.ListenerOptions;
import me.googas.commands.jda.UnknownCommand;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GenericCommandContext;
import me.googas.commands.jda.context.GuildCommandContext;
import me.googas.commands.jda.context.SlashCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
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
    String commandName = strings[0];
    String prefix = listenerOptions.getPrefix(event.isFromGuild() ? event.getGuild() : null);
    if (!commandName.startsWith(prefix)) {
      return;
    }
    this.listenerOptions.preCommand(event, commandName, strings);
    String finalCommandName = commandName.substring(prefix.length());
    JdaCommand command =
        this.getCommand(event.isFromGuild() ? event.getGuild() : null, finalCommandName);
    GenericCommandContext context = this.getCommandContext(event, strings, command);
    Result result = this.getResult(command, finalCommandName, context);
    Message response = this.getMessage(result, context);
    Consumer<Message> consumer = this.getConsumer(result, context);
    if (response != null) {
      if (consumer != null) {
        event
            .getChannel()
            .sendMessage(response)
            .queue(consumer, fail -> this.listenerOptions.handle(fail, context));
      } else {
        event
            .getChannel()
            .sendMessage(response)
            .queue(null, fail -> this.listenerOptions.handle(fail, context));
      }
    }
  }

  /**
   * Execute a command when the event is received.
   *
   * @param event the event of a slash command being received
   */
  @SubscribeEvent
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    event.deferReply().queue();
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
    Result result = this.getResult(command, command.getName(), context);
    Message response = this.getMessage(result, context);
    Consumer<Message> consumer = this.getConsumer(result, context);
    if (response != null) {
      if (consumer != null) {
        event
            .getHook()
            .sendMessage(response)
            .queue(consumer, fail -> this.listenerOptions.handle(fail, context));
      } else {
        event
            .getHook()
            .sendMessage(response)
            .queue(null, fail -> this.listenerOptions.handle(fail, context));
      }
    }
  }

  @NonNull
  private JdaCommand getCommand(Guild guild, @NonNull String name) {
    JdaCommand command = this.manager.getCommand(guild, name);
    return command == null ? new UnknownCommand(this.manager, name) : command;
  }

  /**
   * Get the action to do with the message sent from a result.
   *
   * @param result the result to get the action from
   * @param context the context of the command execution
   * @return the action from the result or null if it doesn't have any
   */
  public Consumer<Message> getConsumer(Result result, @NonNull CommandContext context) {
    return this.listenerOptions.processConsumer(result, context);
  }

  /**
   * Get the message that will be send from a result.
   *
   * @param result the result to get the message from
   * @param context the context of the command execution
   * @return the message
   */
  private Message getMessage(Result result, CommandContext context) {
    return this.listenerOptions.processResult(result, context);
  }

  /**
   * Get the result of a command execution.
   *
   * @param command the command
   * @param commandName the name of the command
   * @param context the context of the command
   * @return the result of the command execution
   */
  private Result getResult(
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
