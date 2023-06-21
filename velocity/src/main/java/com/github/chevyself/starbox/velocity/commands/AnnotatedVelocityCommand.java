package com.github.chevyself.starbox.velocity.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.velocity.VelocityCommandExecutor;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class AnnotatedVelocityCommand
    extends AbstractAnnotatedCommand<CommandContext, VelocityCommand> implements VelocityCommand {

  private final String permission;
  private final boolean async;
  @NonNull private final VelocityCommandExecutor executor;

  public AnnotatedVelocityCommand(
      @NonNull CommandManager<CommandContext, VelocityCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      String permission,
      boolean async) {
    super(commandManager, annotation, object, method);
    this.permission = permission;
    this.async = async;
    this.executor = new VelocityCommandExecutor(this);
  }
}
