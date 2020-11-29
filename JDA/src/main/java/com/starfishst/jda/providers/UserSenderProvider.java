package com.starfishst.jda.providers;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.User;

/** Return the sender in the command arguments */
public class UserSenderProvider implements JdaExtraArgumentProvider<User> {
  @NonNull
  @Override
  public User getObject(@NonNull CommandContext context) {
    return context.getSender();
  }

  @Override
  public @NonNull Class<User> getClazz() {
    return User.class;
  }
}
