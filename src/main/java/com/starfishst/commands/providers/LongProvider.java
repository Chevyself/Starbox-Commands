package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LongProvider extends ArgumentProvider<Long> {

  public LongProvider() {
    super(long.class);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    return new ArrayList<>();
  }

  @NotNull
  @Override
  public Long fromString(@NotNull String string) throws ArgumentProviderException {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException("&e{0} &cis not the correct long format", string);
    }
  }
}
