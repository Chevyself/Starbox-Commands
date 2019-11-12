package com.starfishst.commands.topic;

import com.starfishst.commands.AnnotatedCommand;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
import org.jetbrains.annotations.NotNull;

public class AnnotatedCommandHelpTopicFactory implements HelpTopicFactory<AnnotatedCommand> {

  @Override
  public @NotNull HelpTopic createTopic(@NotNull AnnotatedCommand annotatedCommand) {
    return new AnnotatedCommandHelpTopic(annotatedCommand, null);
  }
}
