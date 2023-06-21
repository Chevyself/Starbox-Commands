package com.github.chevyself.starbox.velocity.messages;

import com.github.chevyself.starbox.common.DecoratedMessagesProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

public class GenericVelocityMessagesProvider extends DecoratedMessagesProvider<CommandContext>
    implements VelocityMessagesProvider {

  @Override
  public @NonNull String onlyPlayers(@NonNull CommandContext context) {
    return String.format("%s%sOnly players can use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return String.format(
        "%s%s %sis not a valid player", this.errorPrefix(), this.element(), this.text());
  }
}
