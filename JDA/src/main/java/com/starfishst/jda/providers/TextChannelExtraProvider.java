package com.starfishst.jda.providers;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link TextChannel} */
public class TextChannelExtraProvider implements JdaExtraArgumentProvider<TextChannel> {

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
