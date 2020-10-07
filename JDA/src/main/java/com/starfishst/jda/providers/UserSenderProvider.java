package com.starfishst.jda.providers;

import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.jda.context.CommandContext;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Return the sender in the command arguments */
public class UserSenderProvider implements IExtraArgumentProvider<User, CommandContext> {
  @NotNull
  @Override
  public User getObject(@NotNull CommandContext context) {
    return context.getSender();
  }

  @Override
  public @NotNull Class<User> getClazz() {
    return User.class;
  }
}
