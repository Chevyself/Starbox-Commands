package me.googas.commands;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;

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
public interface EasyCommandManager<C extends EasyCommandContext, T extends EasyCommand<C, T>> {

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
   * Register the commands contained inside the class of the provided object. This will execute
   * {@link #parseCommands(Object)} to later {@link #registerAll(Collection)}
   *
   * @param object the object to parse the commands from
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  default EasyCommandManager<C, T> register(@NonNull Object object) {
    this.registerAll(this.parseCommands(object));
    return this;
  }

  /**
   * Register all the objects in an array. This will loop around each object to execute {@link
   * #register(Object)}
   *
   * @param objects the objects to parse and register commands from
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  default EasyCommandManager<C, T> registerAll(@NonNull Object... objects) {
    for (Object object : objects) {
      this.register(object);
    }
    return this;
  }

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
  Collection<? extends ReflectCommand<C, T>> parseCommands(@NonNull Object object);

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
  ReflectCommand<C, T> parseCommand(@NonNull Object object, @NonNull Method method);

  /**
   * Registers the collection of commands. This will call {@link #register(EasyCommand)} on loop
   *
   * @param commands the commands to be registered
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  default EasyCommandManager<C, T> registerAll(@NonNull Collection<? extends T> commands) {
    for (T command : commands) {
      this.register(command);
    }
    return this;
  }

  /**
   * Registers the collection of commands. This will call {@link #registerAll(Collection)}
   *
   * @see #register(EasyCommand)
   * @param commands the commands to be registered
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  default EasyCommandManager<C, T> registerAll(@NonNull T... commands) {
    return this.registerAll(Arrays.asList(commands));
  }

  /**
   * Get all the {@link EasyCommand} that are registered in this instance. This will contain all the
   * commands that were registered using {@link #register(EasyCommand)}
   *
   * @return the registered commands.
   */
  @NonNull
  Collection<T> getCommands();

  /**
   * Get the providers registry that this manager may use for {@link EasyCommandContext}
   *
   * @return the providers registry
   */
  @NonNull
  ProvidersRegistry<C> getProvidersRegistry();

  /**
   * Get the messages provider that this manager may use for {@link EasyCommand} messages
   *
   * @return the messages provider
   */
  @NonNull
  EasyMessagesProvider<C> getMessagesProvider();
}
