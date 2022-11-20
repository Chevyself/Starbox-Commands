package chevyself.github.commands.jda.providers.registry;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.jda.providers.CommandContextProvider;
import chevyself.github.commands.jda.providers.GuildCommandContextProvider;
import chevyself.github.commands.jda.providers.GuildProvider;
import chevyself.github.commands.jda.providers.MemberProvider;
import chevyself.github.commands.jda.providers.MessageProvider;
import chevyself.github.commands.jda.providers.RoleProvider;
import chevyself.github.commands.jda.providers.TextChannelProvider;
import chevyself.github.commands.jda.providers.UserProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import lombok.NonNull;

/** The providers' registry for jda. */
public class JdaProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the providers' registry for jda.
   *
   * @param messages the messages' provider for the registry
   */
  public JdaProvidersRegistry(@NonNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider())
        .addProvider(new GuildCommandContextProvider(messages))
        .addProvider(new GuildProvider(messages))
        .addProvider(new MemberProvider(messages))
        .addProvider(new MessageProvider())
        .addProvider(new RoleProvider(messages))
        .addProvider(new TextChannelProvider(messages))
        .addProvider(new UserProvider());
  }
}
