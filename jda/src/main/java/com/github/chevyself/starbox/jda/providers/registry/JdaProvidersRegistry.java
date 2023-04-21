package com.github.chevyself.starbox.jda.providers.registry;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.messages.MessagesProvider;
import com.github.chevyself.starbox.jda.providers.CommandContextProvider;
import com.github.chevyself.starbox.jda.providers.GuildCommandContextProvider;
import com.github.chevyself.starbox.jda.providers.GuildProvider;
import com.github.chevyself.starbox.jda.providers.MemberProvider;
import com.github.chevyself.starbox.jda.providers.MessageProvider;
import com.github.chevyself.starbox.jda.providers.RoleProvider;
import com.github.chevyself.starbox.jda.providers.TextChannelProvider;
import com.github.chevyself.starbox.jda.providers.UserProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
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
