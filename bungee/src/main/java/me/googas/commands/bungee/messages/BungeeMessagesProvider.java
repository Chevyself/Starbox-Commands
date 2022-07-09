package me.googas.commands.bungee.messages;

import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.time.Time;
import me.googas.commands.util.Strings;

/** The default messages provider for bungee. */
public class BungeeMessagesProvider implements MessagesProvider {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not a valid long", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not a valid integer", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not a valid double", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not a valid boolean", string);
  }

  @Override
  public @NonNull String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not valid time", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext context) {
    return "&e&o\u26A0 &c&oYou are missing the argument &4&o"
        + name
        + "&c&o at position &4&o"
        + position
        + "&c&o: &7&o"
        + description;
  }

  @Override
  public @NonNull String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull CommandContext context) {
    return "&e&o\u26A0 &c&oYou are missing at least &4&o"
        + missing
        + "&c&o strings of the argument &4&o"
        + name
        + "&c&o: &7&o"
        + description;
  }

  @Override
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Time timeLeft) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not valid time", string);
  }

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return "&e&o\u26A0 &c&oYou are not allowed to use this command";
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format("&e&o\u26A0 &4&o{0} &c&ois not a valid player", string);
  }

  @Override
  public @NonNull String onlyPlayers(CommandContext context) {
    return "&e&o\u26A0 &c&oConsole cannot use this command";
  }
}
