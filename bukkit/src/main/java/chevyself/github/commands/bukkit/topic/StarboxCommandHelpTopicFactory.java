package chevyself.github.commands.bukkit.topic;

import chevyself.github.commands.StarboxCommand;
import chevyself.github.commands.bukkit.CommandManager;
import chevyself.github.commands.bukkit.StarboxBukkitCommand;
import chevyself.github.commands.bukkit.messages.MessagesProvider;
import lombok.NonNull;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * This {@link HelpTopicFactory} is used for every time that a {@link StarboxCommand} is registered
 * using {@link CommandManager#register(StarboxBukkitCommand)} adds a {@link
 * StarboxCommandHelpTopic} to the {@link org.bukkit.help.HelpMap}
 */
public class StarboxCommandHelpTopicFactory implements HelpTopicFactory<StarboxBukkitCommand> {

  @NonNull private final MessagesProvider provider;

  /**
   * Create the factory.
   *
   * @param provider the messages' provider to format the help topics see {@link
   *     StarboxCommandHelpTopic}
   */
  public StarboxCommandHelpTopicFactory(@NonNull MessagesProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NonNull HelpTopic createTopic(@NonNull StarboxBukkitCommand command) {
    return new StarboxCommandHelpTopic(command, null, this.provider);
  }
}
