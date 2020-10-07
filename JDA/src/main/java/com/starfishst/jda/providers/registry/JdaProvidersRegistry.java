package com.starfishst.jda.providers.registry;

import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.CommandContextProvider;
import com.starfishst.jda.providers.GuildCommandContextProvider;
import com.starfishst.jda.providers.GuildProvider;
import com.starfishst.jda.providers.MemberProvider;
import com.starfishst.jda.providers.MemberSenderProvider;
import com.starfishst.jda.providers.MessageProvider;
import com.starfishst.jda.providers.RoleProvider;
import com.starfishst.jda.providers.TextChannelExtraProvider;
import com.starfishst.jda.providers.TextChannelProvider;
import com.starfishst.jda.providers.UserProvider;
import com.starfishst.jda.providers.UserSenderProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** The providers registry for jda */
public class JdaProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the providers registry for jda
   *
   * @param messages the messages provider for the registry
   */
  public JdaProvidersRegistry(@NotNull MessagesProvider messages) {
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
  public List<IContextualProvider<?, CommandContext>> getProviders(@NotNull Class<?> clazz) {
    return super.getProviders(clazz);
  }
}
