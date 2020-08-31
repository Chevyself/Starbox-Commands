package com.starfishst.bukkit.messages;

import com.starfishst.bukkit.AnnotatedCommand;
import com.starfishst.bukkit.ParentCommand;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.messages.IMessagesProvider;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** Provides the messages for bukkit responses */
public interface MessagesProvider extends IMessagesProvider<CommandContext> {

  /**
   * The message sent when a player is not found
   *
   * @param string the input string querying for a player name
   * @param context the context of the command
   * @return the message to send
   */
  @NotNull
  String invalidPlayer(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message sent when a command is executed by another entity rather than a player
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NotNull
  String playersOnly(@NotNull CommandContext context);

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NotNull
  String notAllowed(@NotNull CommandContext context);

  /**
   * The short message of the help topic in the plugin
   *
   * @param plugin the plugin requesting the short message for its help topic
   * @return the short message of the help topic in the plugin
   */
  @NotNull
  String helpTopicShort(@NotNull Plugin plugin);

  /**
   * A full message of the help topic in the plugin
   *
   * @param shortText the plugin requesting the message for its help topic
   * @param commands the name of every command to append in the help topic
   * @param plugin the plugin that needs the full message for the help topic
   * @return the full message for the help topic
   */
  @NotNull
  String helpTopicFull(@NotNull String shortText, @NotNull String commands, @NotNull Plugin plugin);

  /**
   * Get how a command should be appended in a help topic
   *
   * @param command the command that should be appended in the plugin help topic
   * @return the string that should be appended to give information about a command in the help
   *     topic
   */
  @NotNull
  String helpTopicCommand(@NotNull AnnotatedCommand command);

  /**
   * Get the error message when the selector '@s' is used but the sender was not an entity
   *
   * @param context the context of the command
   * @return the error message
   */
  @NotNull
  String entitiesOnly(@NotNull CommandContext context);

  /**
   * Get the error message when an selector argument is missing its value
   *
   * @param argument the argument that is missing the value
   * @param context the context of the command
   * @return the error message
   */
  @NotNull
  String missingArgumentSelectorSeparator(
      @NotNull String argument, @NotNull CommandContext context);

  /**
   * Get the error message when a selector argument does not exist
   *
   * @param argument the name of the argument which does not exist
   * @param context the context of the command
   * @return the error message
   */
  @NotNull
  String nullSelectorArgument(@NotNull String argument, @NotNull CommandContext context);

  /**
   * The short text for a command help topic
   *
   * @param command the command that is being built the help topic to
   * @return the short text
   */
  @NotNull
  String commandShortText(@NotNull AnnotatedCommand command);

  /**
   * The name of the command in the help topic
   *
   * @param command the command that is being built the help topic to
   * @return the command
   */
  @NotNull
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
  @NotNull
  String parentCommandFull(
      @NotNull ParentCommand command,
      @NotNull String shortText,
      @NotNull String buildChildren,
      @NotNull String buildArguments);

  /**
   * Get the short text for parent commands
   *
   * @param command the parent command that is being built the help topic to
   * @param shortText the short text for every command
   * @return the short text for the parent command
   */
  @NotNull
  String parentCommandShort(@NotNull ParentCommand command, @NotNull String shortText);

  /**
   * Get the full text for a command
   *
   * @param command the child command that is being built the help topic to
   * @param shortText the short text for every command
   * @param buildArguments the arguments of the command
   * @return the built full text for a child command
   */
  @NotNull
  String commandFull(
      @NotNull AnnotatedCommand command, @NotNull String shortText, @NotNull String buildArguments);

  /**
   * Get the name of a child command help topic
   *
   * @param command the child command which requires a name for its help topic
   * @param parent the parent of this child
   * @return the name for the command help topic
   */
  @NotNull
  String childCommandName(@NotNull AnnotatedCommand command, @NotNull ParentCommand parent);

  /**
   * Get the short text for a child command help topic
   *
   * @param command the command which requires a short text for its help topic
   * @param parent the parent of this child
   * @return the short text of the child command help topic
   */
  @NotNull
  String childCommandShort(@NotNull AnnotatedCommand command, @NotNull ParentCommand parent);

  /**
   * Get the full text for a child command help topic
   *
   * @param command the command which requires a full text for its help topic
   * @param parent the parent of this child
   * @param shortText the short text of the child command
   * @param buildArguments the arguments of the command
   * @return the full text for the command help topic
   */
  @NotNull
  String childCommandFull(
      @NotNull AnnotatedCommand command,
      @NotNull ParentCommand parent,
      @NotNull String shortText,
      @NotNull String buildArguments);

  /**
   * Get the help for a required argument
   *
   * @param argument the required argument that needs the help for a command help topic
   * @return the help to append in the command help topic
   */
  @NotNull
  String requiredArgumentHelp(@NotNull Argument<?> argument);

  /**
   * Get the help for an optional argument
   *
   * @param argument the optional argument that needs the help for a command help topic
   * @return the help to append in the command help topic
   */
  @NotNull
  String optionalArgumentHelp(@NotNull Argument<?> argument);

  /**
   * A simple description for a child command for its parents help topic
   *
   * @param command the command that requires the description for its parent
   * @param parent the parent of the command
   * @return the child command description
   */
  @NotNull
  String childCommand(@NotNull AnnotatedCommand command, @NotNull ParentCommand parent);

  /**
   * The message to tell the users that materials have a name and it cannot be empty
   *
   * @param context the context of the command
   * @return the message to tell the user
   */
  @NotNull
  String invalidMaterialEmpty(@NotNull CommandContext context);

  /**
   * The message to tell the users that the material is not found
   *
   * @param string the name of the material that the user queried
   * @param context the context of the command
   * @return the message to tell the user
   */
  @NotNull
  String invalidMaterial(@NotNull String string, @NotNull CommandContext context);
}
