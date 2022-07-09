package me.googas.commands.jda.providers;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.providers.type.JdaArgumentProvider;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link StarboxCommandManager} with a {@link User}. */
public class UserProvider implements JdaArgumentProvider<User>, JdaExtraArgumentProvider<User> {

  /**
   * Returns the id from a user's mention. This will separate characters that are digits to get the
   * id of the {@link User}
   *
   * @see User#getAsMention()
   * @see User#getIdLong()
   * @see Long#parseLong(String)
   * @param mention the user's mention
   * @return the id of the user
   * @throws NumberFormatException if the digits do not match a valid {@link Long}
   */
  public static long getIdFromMention(@NonNull String mention) {
    StringBuilder builder = new StringBuilder();
    for (char c : mention.toCharArray()) {
      if (Character.isDigit(c)) {
        builder.append(c);
      }
    }
    return Long.parseLong(builder.toString());
  }

  @Override
  public @NonNull Class<User> getClazz() {
    return User.class;
  }

  @Override
  public @NonNull User getObject(@NonNull CommandContext context) {
    return context.getSender();
  }

  @NonNull
  @Override
  public User fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    User user = null;
    try {
      user = context.getJda().getUserById(UserProvider.getIdFromMention(string));
    } catch (NumberFormatException e) {
      List<User> usersByName = context.getJda().getUsersByName(string, true);
      if (!usersByName.isEmpty()) {
        user = usersByName.get(0);
      }
    }
    if (user != null) {
      return user;
    } else {
      throw new ArgumentProviderException(
          context.getMessagesProvider().invalidUser(string, context));
    }
  }
}
