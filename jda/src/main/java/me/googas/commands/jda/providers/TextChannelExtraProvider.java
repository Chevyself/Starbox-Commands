package me.googas.commands.jda.providers;

import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.TextChannel;

/** Provides the {@link ICommandManager} with a {@link TextChannel} */
public class TextChannelExtraProvider implements JdaExtraArgumentProvider<TextChannel> {

  @Override
  public @NonNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }

  @NonNull
  @Override
  public TextChannel getObject(@NonNull CommandContext context) {
    return context.getMessage().getTextChannel();
  }
}
