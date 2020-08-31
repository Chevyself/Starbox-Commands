package com.starfishst.bukkit.topic;

import com.starfishst.bukkit.AnnotatedCommand;
import com.starfishst.bukkit.messages.MessagesProvider;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
import org.jetbrains.annotations.NotNull;

/** The factory for {@link AnnotatedCommand} help topics */
public class AnnotatedCommandHelpTopicFactory implements HelpTopicFactory<AnnotatedCommand> {

  /** The messages provider for the commands help topic */
  @NotNull private final MessagesProvider provider;

  /**
   * Create the help topic factory
   *
   * @param provider the messages provider to build the help topics
   */
  public AnnotatedCommandHelpTopicFactory(@NotNull MessagesProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NotNull HelpTopic createTopic(@NotNull AnnotatedCommand annotatedCommand) {
    return new AnnotatedCommandHelpTopic(annotatedCommand, null, provider);
  }
}
