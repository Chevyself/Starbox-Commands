package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DoubleProvider extends ArgumentProvider<Double> {

  public DoubleProvider() {
    super(double.class);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    return new ArrayList<>();
  }

  @NotNull
  @Override
  public Double fromString(@NotNull String string) throws ArgumentProviderException {
    try {
      return Double.parseDouble(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("&e{0} &cis not the correct double format", string);
    }
  }
}
