package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.type.JdaArgumentProvider;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Role} */
public class RoleProvider implements JdaArgumentProvider<Role> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public RoleProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NotNull
  @Override
  public Role fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    for (Role role : context.getMessage().getMentionedRoles()) {
      if (string.contains(role.getId())) {
        return role;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidRole(string, context));
  }

  @Override
  public @NotNull Class<Role> getClazz() {
    return Role.class;
  }
}
