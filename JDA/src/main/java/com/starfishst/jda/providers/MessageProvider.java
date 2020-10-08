package com.starfishst.jda.providers;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/** Provides the command with the message in which it was execute */
public class MessageProvider implements JdaExtraArgumentProvider<Message> {

  @NotNull
  @Override
  public Message getObject(@NotNull CommandContext context) {
    return context.getMessage();
  }

  @Override
  public @NotNull Class<Message> getClazz() {
    return Message.class;
  }
}
