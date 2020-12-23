package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Material;

/** Provides commands with materials */
public class MaterialProvider implements BukkitArgumentProvider<Material> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider the provider of the message in case the material is not found
   */
  public MaterialProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Material> getClazz() {
    return Material.class;
  }

  @NonNull
  @Override
  public Material fromString(@NonNull String string, @NonNull CommandContext context)
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
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    for (Material value : Material.values()) {
      suggestions.add("minecraft:" + value.toString().toLowerCase());
    }
    return suggestions;
  }
}
