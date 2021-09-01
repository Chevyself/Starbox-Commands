package me.googas.commands.jda.context;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/** This context is used for every command {@link User being the sender}. */
public interface CommandContext extends StarboxCommandContext {

  @NonNull
  JDA getJda();

  @NonNull
  Optional<MessageChannel> getChannel();

  @NonNull
  Optional<Message> getMessage();

  @NonNull
  String getCommandName();

  @NonNull
  @Override
  User getSender();

  @Override
  default boolean hasFlag(@NonNull String flag) {
    for (String string : this.getStrings()) {
      if (string.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }

  @Override
  @NonNull
  ProvidersRegistry<CommandContext> getRegistry();

  @Override
  @NonNull
  MessagesProvider getMessagesProvider();
}
