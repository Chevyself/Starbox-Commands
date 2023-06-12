package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import lombok.NonNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface JdaCommand extends StarboxCommand<CommandContext, JdaCommand> {

  @NonNull
  String getDescription();

  @NonNull
  default SlashCommandData toCommandData() {
    SlashCommandData commandData = Commands.slash(this.getName(), this.getDescription());
    this.getChild().stream().map(JdaCommand::toSubcommandData).forEach(commandData::addSubcommands);
    return commandData;
  }

  @NonNull
  default SubcommandData toSubcommandData() {
    return new SubcommandData(this.getName(), this.getDescription());
  }
}
