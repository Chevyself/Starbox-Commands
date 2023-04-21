package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

/** Provides the command with the message in which it was executed. */
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
