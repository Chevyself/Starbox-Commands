package com.starfishst.commands.jda.providers.registry;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.messages.MessagesProvider;
import com.starfishst.commands.jda.providers.CommandContextProvider;
import com.starfishst.commands.jda.providers.GuildCommandContextProvider;
import com.starfishst.commands.jda.providers.GuildProvider;
import com.starfishst.commands.jda.providers.MemberProvider;
import com.starfishst.commands.jda.providers.MemberSenderProvider;
import com.starfishst.commands.jda.providers.MessageProvider;
import com.starfishst.commands.jda.providers.RoleProvider;
import com.starfishst.commands.jda.providers.TextChannelExtraProvider;
import com.starfishst.commands.jda.providers.TextChannelProvider;
import com.starfishst.commands.jda.providers.UserProvider;
import com.starfishst.commands.jda.providers.UserSenderProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.IContextualProvider;
import java.util.List;
import lombok.NonNull;

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
