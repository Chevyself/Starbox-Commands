package chevyself.github.commands.bungee.providers;

import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.providers.type.BungeeExtraArgumentProvider;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

/** Provides the command manager the sender of the command. */
public class CommandSenderProvider implements BungeeExtraArgumentProvider<CommandSender> {

  @NonNull
  @Override
  public CommandSender getObject(@NonNull CommandContext context) {
    return context.getSender();
  }

  @Override
  public @NonNull Class<CommandSender> getClazz() {
    return CommandSender.class;
  }
}
