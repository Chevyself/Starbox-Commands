package com.github.chevyself.starbox.velocity.commands;

import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.velocity.VelocityCommandExecutor;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class VelocityBuiltCommand extends AbstractBuiltCommand<CommandContext, VelocityCommand>
    implements VelocityCommand {

  @NonNull private final VelocityCommandExecutor executor;
  private final String permission;
  private final boolean async;

  public VelocityBuiltCommand(@NonNull CommandBuilder<CommandContext, VelocityCommand> builder) {
    super(builder);
    this.executor = new VelocityCommandExecutor(this);
    this.permission =
        builder.getMetadata().has("permission") ? builder.getMetadata().get("permission") : null;
    this.async = builder.getMetadata().has("async") ? builder.getMetadata().get("async") : false;
  }
}
