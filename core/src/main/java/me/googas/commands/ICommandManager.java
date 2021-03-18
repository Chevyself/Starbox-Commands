package me.googas.commands;

import java.lang.reflect.Method;
import java.util.Collection;
import lombok.NonNull;
import me.googas.commands.context.ICommandContext;

/**
 * This represents the object where {@link ICommand} are registered and queried for execution. The
 * implementation for this variates from module to module. This contains the principal methods which
 * are intended for its use
 *
 * @param <C> the type of command context which is required for the commands to be executed
 */
public interface ICommandManager<C extends ICommandContext> {

  /**
   * Register a new command into the manager
   *
   * @param command the command to be registered
   */
  void register(@NonNull ICommand<C> command);

  /**
   * Parse the {@link ReflectCommand} from the provided object. This depends on each implementation
   * of the command manager.
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
   * Get all the {@link ICommand} that are registered in this instance. This will contain all the
   * commands that were registered using {@link #register(ICommand)}
   *
   * @return the registered commands.
   */
  @NonNull
  Collection<ICommand<C>> getCommands();
}
