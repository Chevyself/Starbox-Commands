package me.googas.commands.bukkit.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import org.bukkit.Material;

/** Provides commands with materials. */
public class MaterialProvider implements BukkitArgumentProvider<Material> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
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
        throw new ArgumentProviderException(this.messagesProvider.invalidMaterialEmpty(context));
      }
      return Material.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidMaterial(string, context));
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    boolean prefix = string.startsWith("minecraft:");
    for (Material value : Material.values()) {
      String name = value.toString().toLowerCase();
      if (prefix) {
        name = "minecraft:" + name;
      }
      suggestions.add(name);
    }
    return suggestions;
  }
}
