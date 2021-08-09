package me.googas.commands.context;

import java.util.Arrays;
import lombok.NonNull;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;

/**
 * The context of the execution of a command. The principal variables that a context requires is the
 * sender and the strings that represent the arguments, obviously, each implementation has a
 * different context but this gives an important idea .
 */
public interface StarboxCommandContext {

  /**
   * Get if the command was executed using the given flag.
   *
   * @param flag the flag to check
   * @return true if the command was executed with the given flag
   */
  boolean hasFlag(@NonNull String flag);

  /**
   * Get the joined strings from a certain position.
   *
   * @param position the position to get the string from
   * @return an array of strings empty if none
   */
  @NonNull
  default String[] getStringsFrom(int position) {
    return Arrays.copyOfRange(this.getStrings(), position, this.getStrings().length);
  }

  /**
   * Get the sender of the command.
   *
   * @return the sender of the command
   */
  @NonNull
  Object getSender();

  /**
   * a Get the joined strings of the command as a single string.
   *
   * @return the joined strings as a String
   */
  @NonNull
  String getString();

  /**
   * Get the joined strings of the command
   *
   * @return the joined strings as an array
   */
  @NonNull
  String[] getStrings();

  /**
   * Get the registry used in this context. This allows to get the arguments of the command.
   *
   * @return the providers registry
   */
  @NonNull
  ProvidersRegistry<? extends StarboxCommandContext> getRegistry();

  /**
   * Get the messages provider used in this context.
   *
   * @return the messages provider used in this context
   */
  @NonNull
  StarboxMessagesProvider<? extends StarboxCommandContext> getMessagesProvider();
}
