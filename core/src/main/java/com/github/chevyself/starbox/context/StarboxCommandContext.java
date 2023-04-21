package com.github.chevyself.starbox.context;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.flags.StarboxFlag;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;

/**
 * The context of the execution of a command. The principal variables that a context requires is the
 * sender and the strings that represent the arguments, obviously, each implementation has a
 * different context but this gives an important idea .
 */
public interface StarboxCommandContext {

  /**
   * Get all the flags that were used in this context.
   *
   * @return a collection of flags
   */
  Collection<FlagArgument> getFlags();

  /**
   * Get a flag with its alias. This will first attempt to get a {@link FlagArgument} if it is not
   * present in this context it will try to get it from the command using {@link
   * StarboxCommand#getOption(String)}. Flags will be matched using {@link
   * StarboxFlag#hasAlias(String)}
   *
   * @param alias the alias to match
   * @return an optional which may hold the flag
   */
  @NonNull
  default Optional<? extends StarboxFlag> getFlag(@NonNull String alias) {
    Optional<FlagArgument> optional =
        this.getFlags().stream().filter(flag -> flag.hasAlias(alias)).findFirst();
    return optional.isPresent() ? optional : this.getCommand().getOption(alias);
  }

  /**
   * Get the value of a flag. This will get the flag using {@link #getFlag(String)}
   *
   * @param alias the alias of the flag to get
   * @return an optional which may contain the value or not
   */
  @NonNull
  default Optional<String> getFlagValue(@NonNull String alias) {
    return this.getFlag(alias).flatMap(StarboxFlag::getValue);
  }

  /**
   * Get the command that is going to be executed using this context.
   *
   * @param <C> the type of context that it accepts
   * @param <T> the type of the command
   * @return the command
   */
  <C extends StarboxCommandContext, T extends StarboxCommand<C, T>> T getCommand();

  /**
   * Get if the command was executed using the given alias.
   *
   * @param alias the alias to check
   * @return true if the command was executed with the given alias
   */
  default boolean hasFlag(@NonNull String alias) {
    return this.getFlags().stream().anyMatch(flag -> flag.hasAlias(alias));
  }

  /**
   * Get the joined strings from a certain position.
   *
   * @param position the position to get the string from
   * @return an array of strings, empty if none
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
   * Get the joined strings of the command.
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
   * Get the messages' provider used in this context.
   *
   * @return the messages' provider used in this context
   */
  @NonNull
  StarboxMessagesProvider<? extends StarboxCommandContext> getMessagesProvider();
}
