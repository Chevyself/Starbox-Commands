package chevyself.github.commands.bukkit.providers.type;

import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
import java.util.List;
import lombok.NonNull;

/** An extension for providers made for bukkit commands. */
public interface BukkitArgumentProvider<O> extends StarboxArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command.
   *
   * @param string the string that is in the argument position at the moment
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull String string, CommandContext context);
}
