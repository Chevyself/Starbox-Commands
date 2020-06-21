package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/** Provides commands with materials */
public class MaterialProvider implements BukkitArgumentProvider<Material> {

  /** The provider of the message in case the material is not found */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider the provider of the message in case the material is not found
   */
  public MaterialProvider(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Material> getClazz() {
    return Material.class;
  }

  @NotNull
  @Override
  public Material fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    try {
      if (string.startsWith("minecraft:")) {
        string = string.substring(10);
      }
      if (string.isEmpty()) {
        throw new ArgumentProviderException(messagesProvider.invalidMaterialEmpty(context));
      }
      return Material.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(messagesProvider.invalidMaterial(string, context));
    }
  }

  @Override
  public @NotNull List<String> getSuggestions(@NotNull String string, CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    for (Material value : Material.values()) {
      suggestions.add("minecraft:" + value.toString().toLowerCase());
    }
    return suggestions;
  }
}
