package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/** Provides the command with the message in which it was execute */
public class MessageProvider implements IExtraArgumentProvider<Message, CommandContext> {

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
