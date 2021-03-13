package me.googas.commands.jda.providers;

import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaArgumentProvider;
import me.googas.commands.ICommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Role;

/** Provides the {@link ICommandManager} with a {@link Role} */
public class RoleProvider implements JdaArgumentProvider<Role> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
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
    for (Role role : context.getMessage().getMentionedRoles()) {
      if (string.contains(role.getId())) {
        return role;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidRole(string, context));
  }

  @Override
  public @NonNull Class<Role> getClazz() {
    return Role.class;
  }
}
