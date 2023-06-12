package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import lombok.Getter;
import lombok.NonNull;

public class JdaParentCommand extends AbstractParentCommand<CommandContext, JdaCommand> implements JdaCommand {

  @NonNull
  @Getter
  private final String description;

  public JdaParentCommand(
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager,
      @NonNull Command annotation, @NonNull String description) {
    super(commandManager, annotation);
    this.description = description;
  }
}
