package me.googas.commands.jda.context;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.jda.JdaCommand;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/** This context is used for every command {@link User being the sender}. */
public interface CommandContext extends StarboxCommandContext {

  @SuppressWarnings("unchecked")
  @Override
  JdaCommand getCommand();

  /**
   * Get the Discord instance in which the manager is running.
   *
   * @return the Discord instance
   */
  @NonNull
  JDA getJda();

  /**
   * Get the channel where the command was executed. This could be null when the command is executed
   * from console.
   *
   * <p>TODO create ConsoleContext
   *
   * @return the optional channel
   */
  @NonNull
  Optional<MessageChannel> getChannel();

  /**
   * Get the message which ran the command. This could be null when the command is executed from
   * console.
   *
   * <p>TODO create ConsoleContext
   *
   * @return the optional message
   */
  @NonNull
  Optional<Message> getMessage();

  @NonNull
  @Override
  User getSender();

  @Override
  @NonNull
  ProvidersRegistry<CommandContext> getRegistry();

  @Override
  @NonNull
  MessagesProvider getMessagesProvider();

  /**
   * Create a children context from this.
   *
   * @return the new context
   */
  @NonNull
  CommandContext getChildren();
}
