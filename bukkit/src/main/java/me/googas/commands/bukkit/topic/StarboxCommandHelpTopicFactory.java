package me.googas.commands.bukkit.topic;

import lombok.NonNull;
import me.googas.commands.StarboxCommand;
import me.googas.commands.bukkit.AnnotatedCommand;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * This {@link HelpTopicFactory} is used for every time that a {@link StarboxCommand} is registered
 * using {@link me.googas.commands.bukkit.CommandManager#register(StarboxBukkitCommand)} adds a
 * {@link StarboxCommandHelpTopic} to the {@link org.bukkit.help.HelpMap}
 */
public class StarboxCommandHelpTopicFactory implements HelpTopicFactory<AnnotatedCommand> {

  @NonNull private final MessagesProvider provider;

  /**
   * Create the factory
   *
   * @param provider the messages provider to format the help topics see {@link
   *     StarboxCommandHelpTopic}
   */
  public StarboxCommandHelpTopicFactory(@NonNull MessagesProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NonNull HelpTopic createTopic(@NonNull AnnotatedCommand annotatedCommand) {
    return new StarboxCommandHelpTopic(annotatedCommand, null, this.provider);
  }
}
