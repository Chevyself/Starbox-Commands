package me.googas.commands.jda.providers.registry;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.CommandContextProvider;
import me.googas.commands.jda.providers.GuildCommandContextProvider;
import me.googas.commands.jda.providers.GuildProvider;
import me.googas.commands.jda.providers.MemberProvider;
import me.googas.commands.jda.providers.MemberSenderProvider;
import me.googas.commands.jda.providers.MessageProvider;
import me.googas.commands.jda.providers.RoleProvider;
import me.googas.commands.jda.providers.TextChannelExtraProvider;
import me.googas.commands.jda.providers.TextChannelProvider;
import me.googas.commands.jda.providers.UserProvider;
import me.googas.commands.jda.providers.UserSenderProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.IContextualProvider;

/** The providers registry for jda */
public class JdaProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the providers registry for jda
   *
   * @param messages the messages provider for the registry
   */
  public JdaProvidersRegistry(@NonNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider());
    this.addProvider(new GuildCommandContextProvider(messages));
    this.addProvider(new GuildProvider(messages));
    this.addProvider(new MemberProvider(messages));
    this.addProvider(new MemberSenderProvider(messages));
    this.addProvider(new MessageProvider());
    this.addProvider(new RoleProvider(messages));
    this.addProvider(new TextChannelExtraProvider());
    this.addProvider(new TextChannelProvider(messages));
    this.addProvider(new UserProvider(messages));
    this.addProvider(new UserSenderProvider());
  }

  /**
   * Get all the providers for the queried class
   *
   * @param clazz the queried class
   * @return a list of providers for the queried class
   */
  @Override
  public List<IContextualProvider<?, CommandContext>> getProviders(@NonNull Class<?> clazz) {
    return super.getProviders(clazz);
  }
}
