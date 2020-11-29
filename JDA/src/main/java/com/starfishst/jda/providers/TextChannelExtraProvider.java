package com.starfishst.jda.providers;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.TextChannel;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link TextChannel} */
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
