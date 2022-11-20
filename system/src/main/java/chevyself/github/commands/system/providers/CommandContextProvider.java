package chevyself.github.commands.system.providers;

import chevyself.github.commands.providers.type.StarboxExtraArgumentProvider;
import chevyself.github.commands.system.CommandManager;
import chevyself.github.commands.system.context.CommandContext;
import lombok.NonNull;

/** Provides the {@link CommandManager} with the object of {@link CommandContext}. */
public class CommandContextProvider
    implements StarboxExtraArgumentProvider<CommandContext, CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @Override
  public @NonNull CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }
}
