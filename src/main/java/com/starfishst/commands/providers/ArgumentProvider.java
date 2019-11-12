package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class ArgumentProvider<O> {

  @NotNull
  private final Class<O> clazz;

  @NotNull
  public ArgumentProvider(@NotNull Class<O> clazz) {
    this.clazz = clazz;
  }

  @NotNull
  public Class<O> getClazz() {
    return clazz;
  }

  @NotNull
  public abstract List<String> suggestions(@NotNull CommandSender sender);

  @NotNull
  public abstract O fromString(@NotNull String string) throws ArgumentProviderException;
}
