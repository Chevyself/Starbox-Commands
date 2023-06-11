package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.NonNull;

/**
 * This class represents a Command which may be executed by a user depending on the implementations
 * it may change.
 *
 * <p>This allows to have children commands which will be recognized using the first parameter of
 * the command as follows:
 *
 * <p>[prefix][parent] [children].
 *
 * <p>This is to give many outputs using a single command
 *
 * @param <C> the context that is required to run the command
 * @param <T> the type of commands that are allowed as children commands
 */
public interface StarboxCommand<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  /**
   * Generates the usage of the command. Commands don't require a name, so, the base of the usage is
   * just the flags and arguments in case it is a {@link ReflectCommand}.
   *
   * @see Option#generateUsage(Collection)
   * @see Argument#generateUsage(List)
   * @param command the command to generate the help
   * @return the usage of the command
   */
  @NonNull
  static String generateUsage(StarboxCommand<?, ?> command) {
    StringBuilder builder = new StringBuilder();
    builder.append(Option.generateUsage(command.getOptions()));
    if (command instanceof ReflectCommand) {
      ReflectCommand<?, ?> reflectCommand = (ReflectCommand<?, ?>) command;
      if (reflectCommand.getArguments().size() > 0) {
        builder.append(Argument.generateUsage(reflectCommand.getArguments()));
      }
    }
    return builder.toString();
  }

  /**
   * Executes the command. This will run all middlewares of the command.
   *
   * @param context the context that is required to execute the command
   * @return the result of the command execution
   */
  default StarboxResult execute(@NonNull C context) {
    List<String> arguments = context.getCommandLineParser().getArguments();
    if (arguments.size() >= 1) {
      Optional<T> optionalCommand = this.getChildren(arguments.get(0));
      if (optionalCommand.isPresent()) {
        T subcommand = optionalCommand.get();
        return subcommand.execute(context.getChildren(subcommand));
      }
    }
    StarboxResult result =
        this.getMiddlewares().stream()
            .map(middleware -> middleware.next(context))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst().orElseGet(() -> this.run(context));
    this.getMiddlewares().forEach(middleware -> middleware.next(context, result));
    return result;
  }

  /**
   * Runs only the command. This is the actual implementation of the logic of the command
   *
   * @param context the context that is required to run the command
   * @return the result of the command execution
   */
  default StarboxResult run(@NonNull C context) {
    throw new UnsupportedOperationException("This command doesn't have a run implementation");
  }

  /**
   * Check if the command can the command be recognized by the given alias. This is used because
   * commands have names and aliases, instead of asking for the name and aliases of the command just
   * check if the command has the alias
   *
   * @param alias the alias to check
   * @return true if this command has the given alias
   */
  boolean hasAlias(@NonNull String alias);

  /**
   * Get help for the command. This will generate a help message using {@link
   * #generateUsage(StarboxCommand)}
   *
   * @param command the command to generate the help
   * @param children the children of the command
   * @param nameSupplier the function that will supply the name of the command
   * @return the help message
   * @param <T> the type of command
   */
  static <T extends StarboxCommand<?, ?>> String genericHelp(
      @NonNull T command,
      @NonNull Collection<T> children,
      @NonNull Function<T, String> nameSupplier) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("usage: ")
        .append(nameSupplier.apply(command))
        .append(" ")
        .append(StarboxCommand.generateUsage(command));
    if (children.size() > 0) {
      builder.append("\nSubcommands:");
      for (T child : children) {
        builder
            .append("\n + ")
            .append(nameSupplier.apply(child))
            .append(" ")
            .append(StarboxCommand.generateUsage(child));
      }
    }
    return builder.toString();
  }

  /**
   * Get a children command by an alias.
   *
   * @param alias the alias to match the command
   * @return a {@link Optional} instance wrapping the nullable children
   * @see StarboxCommand#hasAlias(String)
   */
  @NonNull
  default Optional<T> getChildren(@NonNull String alias) {
    return this.getChildren().stream().filter(child -> child.hasAlias(alias)).findFirst();
  }

  /**
   * Add a children that can be used to run in this.
   *
   * @param command the child command to add
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> addChild(@NonNull T command) {
    this.getChildren().add(command);
    return this;
  }

  /**
   * Get the manager for the cooldown of this command.
   *
   * @return the manager
   */
  @NonNull
  Optional<? extends StarboxCooldownManager<C>> getCooldownManager();

  /**
   * Get the Middlewares that will run before the command.
   *
   * @return the collection of middlewares
   */
  @NonNull
  Collection<? extends Middleware<C>> getMiddlewares();

  /**
   * Get the options which this command may have.
   *
   * @return the collection of options
   * @see Option
   */
  @NonNull
  Collection<? extends Option> getOptions();

  /**
   * Get a specific option based on its alias. This will go through all the {@link Option} in {@link
   * #getOptions()} filtering using {@link Option#hasAlias(String)} to find the first match.
   *
   * @param alias the alias to match
   * @return an optional {@link Option}
   */
  @NonNull
  default Optional<? extends Option> getOption(@NonNull String alias) {
    return this.getOptions().stream().filter(option -> option.hasAlias(alias)).findFirst();
  }

  /**
   * Get the collection of registered children in this parent. All the children added in this
   * collection add from {@link #addChild(StarboxCommand)}
   *
   * @return the collection of children
   */
  @NonNull
  Collection<T> getChildren();
}
