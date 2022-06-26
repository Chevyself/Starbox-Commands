package me.googas.commands.jda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.Result;

public class UnknownCommand extends JdaCommand {

  @NonNull private final String name;

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
