package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
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

  /**
   * Get a discord id from a string
   *
   * @param string the string to get the id from
   * @return the id
   */
  public static long getIdFromString(@NotNull String string) {
    return Long.parseLong(string.replace("<", "").replace(">", "").replace("@!", ""));
  }

  @Override
  public @NotNull Class<User> getClazz() {
    return User.class;
  }

  @NotNull
  @Override
  public User fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    User user;
    try {
      user = context.getMessage().getJDA().retrieveUserById(getIdFromString(string)).complete();
    } catch (NumberFormatException e) {
      user = null;
    }
    if (user != null) {
      return user;
    } else {
      throw new ArgumentProviderException(messagesProvider.invalidUser(string, context));
    }
  }
}
