package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import lombok.Getter;
import lombok.NonNull;

/** Extension of {@link AbstractParentCommand} for JDA. */
@Getter
public class JdaParentCommand extends AbstractParentCommand<CommandContext, JdaCommand>
    implements JdaCommand {

  @NonNull private final String description;

  /**
   * Create the command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param description the description
   */
  public JdaParentCommand(
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager,
      @NonNull Command annotation,
      @NonNull String description) {
    super(commandManager, annotation);
    this.description = description;
  }
}
