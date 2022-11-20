package chevyself.github.commands.jda;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.result.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;

/** This represents a command which wasn't found in {@link CommandManager}. */
public class UnknownCommand extends JdaCommand {

  @NonNull private final String name;

  /**
   * Construct the command.
   *
   * @param manager the manager in which the command wasn't found
   * @param name the name of the command that was being looked for
   */
  public UnknownCommand(@NonNull CommandManager manager, @NonNull String name) {
    super(
        manager,
        "No description provided",
        new HashMap<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        null);
    this.name = name;
  }

  @Override
  public @NonNull Collection<JdaCommand> getChildren() {
    return new ArrayList<>();
  }

  @Override
  public @NonNull List<String> getAliases() {
    return Collections.singletonList(name);
  }

  @Override
  Result run(@NonNull CommandContext context) {
    return null;
  }
}
