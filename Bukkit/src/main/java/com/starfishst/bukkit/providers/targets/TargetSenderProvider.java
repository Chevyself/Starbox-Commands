package com.starfishst.bukkit.providers.targets;

import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.targets.argument.parser.ArgumentParser;
import org.jetbrains.annotations.NotNull;

public class TargetSenderProvider extends TargetSelectorProvider {

  /**
   * Create the target selector provider
   *
   * @param messagesProvider the messages provider for error messages
   * @param parser to parse the selector arguments
   */
  public TargetSenderProvider(
      @NotNull MessagesProvider messagesProvider, @NotNull ArgumentParser parser) {
    super(messagesProvider, parser);
  }
}
