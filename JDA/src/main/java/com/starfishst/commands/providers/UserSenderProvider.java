package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Return the sender in the command arguments */
public class UserSenderProvider implements IExtraArgumentProvider<User, CommandContext> {
  @NotNull
  @Override
  public User getObject(@NotNull CommandContext context) throws ArgumentProviderException {
    return context.getSender();
  }

  @Override
  public @NotNull Class<User> getClazz() {
    return User.class;
  }
}
