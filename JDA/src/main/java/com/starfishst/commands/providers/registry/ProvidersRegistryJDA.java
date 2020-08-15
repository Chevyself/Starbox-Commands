package com.starfishst.commands.providers.registry;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.providers.CommandContextProvider;
import com.starfishst.commands.providers.GuildCommandContextProvider;
import com.starfishst.commands.providers.GuildProvider;
import com.starfishst.commands.providers.MemberProvider;
import com.starfishst.commands.providers.MemberSenderProvider;
import com.starfishst.commands.providers.MessageProvider;
import com.starfishst.commands.providers.RoleProvider;
import com.starfishst.commands.providers.TextChannelExtraProvider;
import com.starfishst.commands.providers.TextChannelProvider;
import com.starfishst.commands.providers.UserProvider;
import com.starfishst.commands.providers.UserSenderProvider;
import com.starfishst.core.providers.BooleanProvider;
import com.starfishst.core.providers.DoubleProvider;
import com.starfishst.core.providers.IntegerProvider;
import com.starfishst.core.providers.JoinedNumberProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.TimeProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** The providers registry for jda */
public class ProvidersRegistryJDA extends ProvidersRegistry<CommandContext> {

  /**
   * Create the providers registry for jda
   *
   * @param messagesProvider the messages provider for the registry
   */
  public ProvidersRegistryJDA(@NotNull MessagesProvider messagesProvider) {
    this.addProvider(new BooleanProvider<>(messagesProvider));
    this.addProvider(new DoubleProvider<>(messagesProvider));
    this.addProvider(new IntegerProvider<>(messagesProvider));
    this.addProvider(new JoinedStringsProvider<>());
    this.addProvider(new LongProvider<>(messagesProvider));
    this.addProvider(new StringProvider<>());
    this.addProvider(new TimeProvider<>(messagesProvider));
    this.addProvider(new CommandContextProvider());
    this.addProvider(new GuildCommandContextProvider(messagesProvider));
    this.addProvider(new GuildProvider(messagesProvider));
    this.addProvider(new MemberProvider(messagesProvider));
    this.addProvider(new MemberSenderProvider(messagesProvider));
    this.addProvider(new MessageProvider());
    this.addProvider(new RoleProvider(messagesProvider));
    this.addProvider(new TextChannelExtraProvider());
    this.addProvider(new TextChannelProvider(messagesProvider));
    this.addProvider(new UserProvider(messagesProvider));
    this.addProvider(new UserSenderProvider());
    this.addProvider(new JoinedNumberProvider<>(messagesProvider));
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
