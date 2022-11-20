package chevyself.github.commands.bukkit.providers.type;

import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxMultipleArgumentProvider;
import java.util.List;
import lombok.NonNull;

/**
 * An extension for a providers made for bukkit commands.
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitMultiArgumentProvider<O>
    extends StarboxMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command.
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull CommandContext context);
}
