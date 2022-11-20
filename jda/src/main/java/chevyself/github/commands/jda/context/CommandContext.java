package chevyself.github.commands.jda.context;

import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.jda.JdaCommand;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import java.util.Optional;
import lombok.NonNull;
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
