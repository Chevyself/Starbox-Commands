package com.github.chevyself.starbox.bungee.messages;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import com.github.chevyself.starbox.util.Strings;
import java.time.Duration;
import lombok.NonNull;

/** The default messages provider for bungee. */
public class GenericBungeeMessagesProvider implements BungeeMessagesProvider {

  private static final String errorPrefix = "&e&o⚠";

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not a valid long", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not a valid integer", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not a valid double", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not a valid boolean", string);
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not valid time", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext context) {
    return GenericBungeeMessagesProvider.errorPrefix
        + "&c&oYou are missing the argument &4&o"
        + name
        + "&c&o at position &4&o"
        + position
        + "&c&o: &7&o"
        + description;
  }

  @Override
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Duration timeLeft) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not valid time",
        TimeUtil.toString(timeLeft));
  }

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return GenericBungeeMessagesProvider.errorPrefix
        + "&c&oYou are not allowed to use this command";
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        GenericBungeeMessagesProvider.errorPrefix + "&4&o{0} &c&ois not a valid player", string);
  }

  @Override
  public @NonNull String onlyPlayers(CommandContext context) {
    return GenericBungeeMessagesProvider.errorPrefix + "&c&oConsole cannot use this command";
  }
}
