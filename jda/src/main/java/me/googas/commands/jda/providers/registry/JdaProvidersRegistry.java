package me.googas.commands.jda.providers.registry;

import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.CommandContextProvider;
import me.googas.commands.jda.providers.GuildCommandContextProvider;
import me.googas.commands.jda.providers.GuildProvider;
import me.googas.commands.jda.providers.MemberProvider;
import me.googas.commands.jda.providers.MessageProvider;
import me.googas.commands.jda.providers.RoleProvider;
import me.googas.commands.jda.providers.TextChannelProvider;
import me.googas.commands.jda.providers.UserProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;

/** The providers registry for jda. */
public class JdaProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the providers registry for jda.
   *
   * @param messages the messages provider for the registry
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
        .addProvider(new UserProvider(messages));
  }
}
