package chevyself.github.commands.jda.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.jda.providers.type.JdaArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Role;

/** Provides the {@link StarboxCommandManager} with a {@link Role}. */
public class RoleProvider implements JdaArgumentProvider<Role> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public RoleProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Role fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    Role role;
    try {
      role = context.getJda().getRoleById(UserProvider.getIdFromMention(string));
    } catch (NumberFormatException ignored) {
      role = null;
    }
    if (role != null) {
      return role;
    }
    throw new ArgumentProviderException(this.messagesProvider.invalidRole(string, context));
  }

  @Override
  public @NonNull Class<Role> getClazz() {
    return Role.class;
  }
}
