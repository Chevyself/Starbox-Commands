package com.starfishst.bukkit.targets.argument.parser;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.exception.SelectorArgumentException;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.targets.TargetRandomPlayer;
import com.starfishst.bukkit.targets.argument.TargetArgument;
import com.starfishst.core.utils.NullableAtomic;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultArgumentParser implements ArgumentParser {

  /** The target arguments that can be used */
  @NotNull private final List<TargetArgument> defaults;
  /** The messages provider for error messages */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create the argument parser
   *
   * @param defaults the default arguments that can be used
   * @param messagesProvider the messages provider for error messages
   */
  public DefaultArgumentParser(
      @NotNull List<TargetArgument> defaults, @NotNull MessagesProvider messagesProvider) {
    this.defaults = defaults;
    this.messagesProvider = messagesProvider;
  }

  /**
   * Applies all the arguments inside the string and gives the targets
   *
   * @param initial the initial targets
   * @param string the string to parse the arguments
   * @param context the context of the command
   * @return the targets
   * @param <E> the type of entity
   * @throws SelectorArgumentException in case that the arguments are incorrect
   */
  @NotNull
  public <E extends Entity> List<E> apply(
      @NotNull List<E> initial, @NotNull String string, @NotNull CommandContext context)
      throws SelectorArgumentException {
    LinkedHashMap<TargetArgument, String> arguments = fromString(string, context);
    NullableAtomic<SelectorArgumentException> atomic = new NullableAtomic<>();
    arguments.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            (entry) -> {
              try {
                entry.getKey().apply(initial, entry.getValue(), arguments, context);
              } catch (SelectorArgumentException e) {
                atomic.set(e);
              }
            });
    SelectorArgumentException exception = atomic.get();
    if (exception != null) {
      throw exception;
    } else {
      return initial;
    }
  }

  /**
   * Get one of the default target arguments from an alias
   *
   * @param alias the alias to match to a target argument
   * @return the target argument if found
   */
  @Nullable
  public TargetArgument getArgument(@NotNull String alias) {
    return defaults.stream()
        .filter(
            argument -> {
              for (String argumentAlias : argument.getAliases()) {
                return argumentAlias.equalsIgnoreCase(alias);
              }
              return false;
            })
        .findFirst()
        .orElse(null);
  }

  @Override
  public @NotNull LinkedHashMap<TargetArgument, String> fromString(
      @NotNull String string, @NotNull CommandContext context) throws SelectorArgumentException {
    string = string.replace("[", "").replace("]", "");
    String[] arguments = string.split(",");
    LinkedHashMap<TargetArgument, String> parsedArguments = new LinkedHashMap<>();
    for (String argument : arguments) {
      if (argument.contains("=")) {
        String[] split = argument.split("=");
        String key = split[0];
        String value = split[1];
        if (value.isEmpty()) {
          throw new SelectorArgumentException(
              messagesProvider.missingArgumentSelectorSeparator(argument, context));
        } else {
          TargetArgument parsedArgument = getArgument(key);
          if (parsedArgument != null) {
            parsedArguments.put(parsedArgument, argument);
          } else {
            throw new SelectorArgumentException(
                messagesProvider.nullSelectorArgument(key, context));
          }
        }
      } else {
        throw new SelectorArgumentException(
            messagesProvider.missingArgumentSelectorSeparator(argument, context));
      }
    }
    return parsedArguments;
  }

  @Override
  public @NotNull TargetRandomPlayer random(@NotNull String string, @NotNull CommandContext context)
      throws SelectorArgumentException {
    return new TargetRandomPlayer(
        apply(new ArrayList<>(Bukkit.getOnlinePlayers()), string, context));
  }
}
