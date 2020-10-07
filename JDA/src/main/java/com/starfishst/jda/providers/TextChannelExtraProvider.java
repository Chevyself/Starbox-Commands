package com.starfishst.jda.providers;

import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.jda.context.CommandContext;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link TextChannel} */
public class TextChannelExtraProvider
    implements IExtraArgumentProvider<TextChannel, CommandContext> {

  @Override
  public @NotNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }

  @NotNull
  @Override
  public TextChannel getObject(@NotNull CommandContext context) {
    return context.getMessage().getTextChannel();
  }
}
