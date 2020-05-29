package com.starfishst.bukkit.context;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Strings;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/** The context of a bukkit command */
public class CommandContext implements ICommandContext<CommandSender> {

  /** The sender of the command */
  @NotNull private final CommandSender sender;
  /** The command line that the sender executed */
  @NotNull private final String string;
  /** The command line slitted */
  @NotNull private final String[] strings;

  /**
   * Create a bukkit context
   *
   * @param sender the sender of the bukkit command
   * @param strings the strings from the command execution
   */
  public CommandContext(@NotNull CommandSender sender, @NotNull String[] strings) {
    this.sender = sender;
    this.string = Strings.fromArray(strings);
    this.strings = strings;
  }

  @NotNull
  @Override
  public CommandSender getSender() {
    return this.sender;
  }

  @NotNull
  @Override
  public String getString() {
    return this.string;
  }

  @NotNull
  @Override
  public String[] getStrings() {
    return this.strings;
  }

  @Override
  public boolean hasFlag(@NotNull String flag) {
    for (String string : this.strings) {
      if (string.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  @Override
  public String[] getStringsFrom(int position) {
    return Lots.arrayFrom(position, this.strings);
  }
}