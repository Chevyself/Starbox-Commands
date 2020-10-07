package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link User} */
public class UserProvider implements IArgumentProvider<User, CommandContext> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public UserProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<User> getClazz() {
    return User.class;
  }

  @NotNull
  @Override
  public User fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    for (User user : context.getMessage().getMentionedUsers()) {
      if (string.contains(user.getId())) {
        return user;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidUser(string, context));
  }
}
