package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Role} */
public class RoleProvider implements IArgumentProvider<Role, CommandContext> {

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
    Role role =
        context.getMessage().getMentionedRoles().stream()
            .filter(mentionedRole -> string.contains(mentionedRole.getId()))
            .findFirst()
            .orElse(null);
    if (role != null) {
      return role;
    } else {
      throw new ArgumentProviderException(messagesProvider.invalidRole(string, context));
    }
  }

  @Override
  public @NotNull Class<Role> getClazz() {
    return Role.class;
  }
}
