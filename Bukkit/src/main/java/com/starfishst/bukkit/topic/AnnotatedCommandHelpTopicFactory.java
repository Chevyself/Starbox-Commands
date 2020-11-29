package com.starfishst.bukkit.topic;

import com.starfishst.bukkit.AnnotatedCommand;
import com.starfishst.bukkit.messages.MessagesProvider;
import lombok.NonNull;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/** The factory for {@link AnnotatedCommand} help topics */
public class AnnotatedCommandHelpTopicFactory implements HelpTopicFactory<AnnotatedCommand> {

  /** The messages provider for the commands help topic */
  @NonNull private final MessagesProvider provider;

  /**
   * Create the help topic factory
   *
   * @param provider the messages provider to build the help topics
   */
  public AnnotatedCommandHelpTopicFactory(@NonNull MessagesProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NonNull HelpTopic createTopic(@NonNull AnnotatedCommand annotatedCommand) {
    return new AnnotatedCommandHelpTopic(annotatedCommand, null, provider);
  }
}
