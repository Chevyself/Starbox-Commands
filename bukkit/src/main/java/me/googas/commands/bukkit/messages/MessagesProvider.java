package me.googas.commands.bukkit.messages;

import lombok.NonNull;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.messages.StarboxMessagesProvider;
import org.bukkit.plugin.Plugin;

/** Provides the messages for bukkit responses */
public interface MessagesProvider extends StarboxMessagesProvider<CommandContext> {

  /**
   * The message sent when a player is not found
   *
   * @param string the input string querying for a player name
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String invalidPlayer(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a command is executed by another entity rather than a player
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String playersOnly(@NonNull CommandContext context);

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String notAllowed(@NonNull CommandContext context);

  /**
   * The short message of the help topic in the plugin
   *
   * @param plugin the plugin requesting the short message for its help topic
   * @return the short message of the help topic in the plugin
   */
  @NonNull
  String helpTopicShort(@NonNull Plugin plugin);

  /**
   * A full message of the help topic in the plugin
   *
   * @param shortText the plugin requesting the message for its help topic
   * @param commands the name of every command to append in the help topic
   * @param plugin the plugin that needs the full message for the help topic
   * @return the full message for the help topic
   */
  @NonNull
  String helpTopicFull(@NonNull String shortText, @NonNull String commands, @NonNull Plugin plugin);

  /**
   * Get how a command should be appended in a help topic
   *
   * @param command the command that should be appended in the plugin help topic
   * @return the string that should be appended to give information about a command in the help
   *     topic
   */
  @NonNull
  String helpTopicCommand(@NonNull StarboxBukkitCommand command);

  /**
   * The short text for a command help topic
   *
   * @param command the command that is being built the help topic to
   * @return the short text
   */
  @NonNull
  String commandShortText(@NonNull StarboxBukkitCommand command);

  /**
   * The name of the command in the help topic
   *
   * @param command the command that is being built the help topic to
   * @param parentName the name of the parent of the command which can be null to be appended to
   *     differentiate from other commands
   * @return the command
   */
  @NonNull
  String commandName(StarboxBukkitCommand command, String parentName);

  /**
   * Get the full text of a help topic for a parent command
   *
   * @param command the parent command that is being built the help topic to
   * @param shortText the short text of the command help topic
   * @param buildChildren the children commands of the parent
   * @param buildArguments the arguments of the parent command
   * @return the full text of the help topic
   */
  @NonNull
  String commandFullText(
      @NonNull StarboxBukkitCommand command,
      @NonNull String shortText,
      @NonNull String buildChildren,
      @NonNull String buildArguments);

  /**
   * A simple description for a child command for its parents help topic
   *
   * @param command the command that requires the description for its parent
   * @param parent the parent of the command
   * @return the child command description
   */
  @NonNull
  String childCommand(@NonNull StarboxBukkitCommand command, @NonNull StarboxBukkitCommand parent);

  /**
   * The message to tell the users that materials have a name and it cannot be empty
   *
   * @param context the context of the command
   * @return the message to tell the user
   */
  @NonNull
  String invalidMaterialEmpty(@NonNull CommandContext context);

  /**
   * The message to tell the users that the material is not found
   *
   * @param string the name of the material that the user queried
   * @param context the context of the command
   * @return the message to tell the user
   */
  @NonNull
  String invalidMaterial(@NonNull String string, @NonNull CommandContext context);
}
