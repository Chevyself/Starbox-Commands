package com.starfishst.bukkit.providers.targets;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.targets.TargetSelector;
import com.starfishst.bukkit.targets.argument.parser.ArgumentParser;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TargetRandomPlayerProvider extends TargetSelectorProvider {

  /**
   * Create the target selector provider
   *
   * @param messagesProvider the messages provider for error messages
   * @param parser to parse the selector arguments
   */
  public TargetRandomPlayerProvider(
      @NotNull MessagesProvider messagesProvider, @NotNull ArgumentParser parser) {
    super(messagesProvider, parser);
  }

  @Override
  public @NotNull TargetSelector fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    return super.fromString(string, context);
  }

  @Override
  public @NotNull List<String> getSuggestions(
      @NotNull String string, @NotNull CommandContext context) {
    if (string.startsWith("@")) {

    } else {

    }
    return super.getSuggestions(string, context);
  }
}
