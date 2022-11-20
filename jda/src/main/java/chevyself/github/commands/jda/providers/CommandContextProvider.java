package chevyself.github.commands.jda.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.context.GenericCommandContext;
import chevyself.github.commands.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;

/** Provides the {@link StarboxCommandManager} with a {@link GenericCommandContext}. */
public class CommandContextProvider implements JdaExtraArgumentProvider<CommandContext> {

  @NonNull
  @Override
  public CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}
