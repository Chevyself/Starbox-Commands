package com.starfishst.core.context;

import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Lots;
import org.jetbrains.annotations.NotNull;

/** The context of a command */
public interface ICommandContext {

  /**
   * Get's if the command was executed using a flag
   *
   * @param flag the flag to check
   * @return true if the command was executed with a flag
   */
  boolean hasFlag(@NotNull String flag);

  /**
   * Get the joined strings from a certain position
   *
   * @param position the position to get the string from
   * @return an array of strings empty if none
   */
  @NotNull
  default String[] getStringsFrom(int position) {
    return Lots.arrayFrom(position, this.getStrings());
  }

  /**
   * Get the sender of the command
   *
   * @return the sender of the command
   */
  @NotNull
  Object getSender();

  /**
   * Get the joined strings of the command
   *
   * @return the joined strings
   */
  @NotNull
  String getString();

  /**
   * Get the joined strings of the command
   *
   * @return the joined strings
   */
  @NotNull
  String[] getStrings();

  /**
   * Get the registry used in this context
   *
   * @return the registry
   */
  ProvidersRegistry<? extends ICommandContext> getRegistry();

  /**
   * Get the messages provider used in this context
   *
   * @return the messages provider used in this context
   */
  IMessagesProvider<? extends ICommandContext> getMessagesProvider();
}
