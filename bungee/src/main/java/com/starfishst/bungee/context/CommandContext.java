package com.starfishst.bungee.context;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Strings;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/** The context for bungee commands */
public class CommandContext implements ICommandContext<CommandSender> {

  /** The sender of bungee commands */
  @NotNull private final CommandSender sender;
  /** The command line */
  @NotNull private final String string;
  /** The command line in separated strings */
  @NotNull private final String[] strings;

  /**
   * Create an instance
   *
   * @param sender the sender of the command
   * @param strings the command line in separated strings
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
