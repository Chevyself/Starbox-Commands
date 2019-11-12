package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class IntegerProvider extends ArgumentProvider<Integer> {

  public IntegerProvider() {
    super(int.class);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    return new ArrayList<>();
  }

  @Override
  public @NotNull Integer fromString(@NotNull String string) throws ArgumentProviderException {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("&e{0} &cis not the correct integer format", string);
    }
  }
}
