package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.jda.JdaCommand;
import com.github.chevyself.starbox.jda.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

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
   * @return the optional channel
   */
  @NonNull
  Optional<MessageChannel> getChannel();

  /**
   * Get the message which ran the command. This could be null when the command is executed from
   * console.
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
  ProvidersRegistry<CommandContext> getProvidersRegistry();

  @Override
  @NonNull
  MessagesProvider getMessagesProvider();

  /**
   * Create a children context from this.
   *
   * @param command the command for which the context is created
   * @return the new context
   */
  @NonNull
  CommandContext getChildren(@NonNull JdaCommand command);

  @Override
  default @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.getProvidersRegistry();
  }
}
