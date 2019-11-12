package com.starfishst.commands.providers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StringProvider extends ArgumentProvider<String> {

  public StringProvider() {
    super(String.class);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    return new ArrayList<>();
  }

  @NotNull
  @Override
  public String fromString(@NotNull String string) {
    return string;
  }
}
