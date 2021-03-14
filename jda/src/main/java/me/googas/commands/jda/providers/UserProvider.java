package me.googas.commands.jda.providers;

import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaArgumentProvider;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link ICommandManager} with a {@link User} */
public class UserProvider implements JdaArgumentProvider<User> {

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
  public @NonNull Class<User> getClazz() {
    return User.class;
  }

  @NonNull
  @Override
  public User fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    for (User user : context.getMessage().getMentionedUsers()) {
      if (string.contains(user.getId())) {
        return user;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidUser(string, context));
  }
}
