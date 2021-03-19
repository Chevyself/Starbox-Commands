package me.googas.commands;

import java.lang.reflect.Method;
import java.util.Collection;
import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;

/**
 * This represents the object where {@link EasyCommand} are registered and queried for execution.
 * The implementation for this variates from module to module. This contains the principal methods
 * which are intended for its use
 *
 * <p>// TODO example using one of the command managers implementations
 *
 * @param <C> the type of command context that is used to run the commands
 * @param <T> the type of command that this instance manages
 */
public interface EasyCommandManager<C extends EasyCommandContext, T extends EasyCommand<C>> {

  /**
   * Register a new command into the manager. Any command that implements the type T can be
   * registered.
   *
   * @param command the command to be registered
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  EasyCommandManager<C, T> register(@NonNull T command);

  /**
   * Parse the {@link ReflectCommand} from the provided object. This depends on each implementation
   * of the command manager.
   *
   * <p>// TODO example from one of the implementations
   *
   * @param object the object to get the commands from
   * @return the collection of parsed commands.
   */
  @NonNull
  Collection<ReflectCommand<C>> parseCommands(@NonNull Object object);

  /**
   * Parse a reflective command using the method where it will be executed and the method instance
   * that must be used to execute the method.
   *
   * <p>// TODO add example using a module
   *
   * @param object the object instance required for the command execution
   * @param method the method used to execute the command
   * @return the parsed command
   */
  @NonNull
  ReflectCommand<C> parseCommand(@NonNull Object object, @NonNull Method method);

  /**
   * Get all the {@link EasyCommand} that are registered in this instance. This will contain all the
   * commands that were registered using {@link #register(EasyCommand)}
   *
   * @return the registered commands.
   */
  @NonNull
  Collection<T> getCommands();
}
