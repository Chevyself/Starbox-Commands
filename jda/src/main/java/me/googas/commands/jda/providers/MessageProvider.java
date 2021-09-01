package me.googas.commands.jda.providers;

import lombok.NonNull;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Message;

/** Provides the command with the message in which it was execute. */
public class MessageProvider implements JdaExtraArgumentProvider<Message> {

  @NonNull
  @Override
  public Message getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (context.getMessage().isPresent()) {
      return context.getMessage().get();
    }
    throw new ArgumentProviderException(context.getMessagesProvider().noMessage(context));
  }

  @Override
  public @NonNull Class<Message> getClazz() {
    return Message.class;
  }
}
