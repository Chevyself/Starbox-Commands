package chevyself.github.commands.bukkit.providers;

import chevyself.github.commands.bukkit.CommandManager;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.messages.MessagesProvider;
import chevyself.github.commands.bukkit.providers.type.BukkitArgumentProvider;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Material;

/** Provides the {@link CommandManager} with the object {@link Material}. */
public class MaterialProvider implements BukkitArgumentProvider<Material> {

  @NonNull private final MessagesProvider messagesProvider;

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
