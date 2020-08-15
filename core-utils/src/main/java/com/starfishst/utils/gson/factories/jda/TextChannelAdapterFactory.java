package com.starfishst.utils.gson.factories.jda;

import com.starfishst.utils.gson.factories.InterfaceTypeAdapterFactory;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

/** The type adapter factory for text channel */
public class TextChannelAdapterFactory implements InterfaceTypeAdapterFactory<TextChannel> {
  /**
   * Get the class of the interface
   *
   * @return the class of the interface
   */
  @Override
  public @NotNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }
}
