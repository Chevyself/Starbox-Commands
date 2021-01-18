package com.starfishst.commands.bukkit.messages;

import com.starfishst.commands.bukkit.AnnotatedCommand;
import com.starfishst.commands.bukkit.ParentCommand;
import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.messages.IMessagesProvider;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/** Provides the messages for bukkit responses */
public interface MessagesProvider extends IMessagesProvider<CommandContext> {

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
  String helpTopicCommand(@NonNull AnnotatedCommand command);

  /**
   * The short text for a command help topic
   *
   * @param command the command that is being built the help topic to
   * @return the short text
   */
  @NonNull
  String commandShortText(@NonNull AnnotatedCommand command);

  /**
   * The name of the command in the help topic
   *
   * @param command the command that is being built the help topic to
   * @return the command
   */
  @NonNull
  String commandName(AnnotatedCommand command);

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
  String parentCommandFull(
      @NonNull ParentCommand command,
      @NonNull String shortText,
      @NonNull String buildChildren,
      @NonNull String buildArguments);

  /**
   * Get the short text for parent commands
   *
   * @param command the parent command that is being built the help topic to
   * @param shortText the short text for every command
   * @return the short text for the parent command
   */
  @NonNull
  String parentCommandShort(@NonNull ParentCommand command, @NonNull String shortText);

  /**
   * Get the full text for a command
   *
   * @param command the child command that is being built the help topic to
   * @param shortText the short text for every command
   * @param buildArguments the arguments of the command
   * @return the built full text for a child command
   */
  @NonNull
  String commandFull(
      @NonNull AnnotatedCommand command, @NonNull String shortText, @NonNull String buildArguments);

  /**
   * Get the name of a child command help topic
   *
   * @param command the child command which requires a name for its help topic
   * @param parent the parent of this child
   * @return the name for the command help topic
   */
  @NonNull
  String childCommandName(@NonNull AnnotatedCommand command, @NonNull ParentCommand parent);

  /**
   * Get the short text for a child command help topic
   *
   * @param command the command which requires a short text for its help topic
   * @param parent the parent of this child
   * @return the short text of the child command help topic
   */
  @NonNull
  String childCommandShort(@NonNull AnnotatedCommand command, @NonNull ParentCommand parent);

  /**
   * Get the full text for a child command help topic
   *
   * @param command the command which requires a full text for its help topic
   * @param parent the parent of this child
   * @param shortText the short text of the child command
   * @param buildArguments the arguments of the command
   * @return the full text for the command help topic
   */
  @NonNull
  String childCommandFull(
      @NonNull AnnotatedCommand command,
      @NonNull ParentCommand parent,
      @NonNull String shortText,
      @NonNull String buildArguments);

  /**
   * Get the help for a required argument
   *
   * @param argument the required argument that needs the help for a command help topic
   * @return the help to append in the command help topic
   */
  @NonNull
  String requiredArgumentHelp(@NonNull Argument<?> argument);

  /**
   * Get the help for an optional argument
   *
   * @param argument the optional argument that needs the help for a command help topic
   * @return the help to append in the command help topic
   */
  @NonNull
  String optionalArgumentHelp(@NonNull Argument<?> argument);

  /**
   * A simple description for a child command for its parents help topic
   *
   * @param command the command that requires the description for its parent
   * @param parent the parent of the command
   * @return the child command description
   */
  @NonNull
  String childCommand(@NonNull AnnotatedCommand command, @NonNull ParentCommand parent);

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
