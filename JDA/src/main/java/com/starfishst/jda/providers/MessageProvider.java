package com.starfishst.jda.providers;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

/** Provides the command with the message in which it was execute */
public class MessageProvider implements JdaExtraArgumentProvider<Message> {

  @NonNull
  @Override
  public Message getObject(@NonNull CommandContext context) {
    return context.getMessage();
  }

  @Override
  public @NonNull Class<Message> getClazz() {
    return Message.class;
  }
}
