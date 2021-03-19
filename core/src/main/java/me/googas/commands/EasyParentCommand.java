package me.googas.commands;

import java.util.Collection;
import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;

/**
 * This represents a parent command. A parent command contains children commands which will be
 * recognized using the first parameter of the command as follows:
 *
 * <p>[prefix][parent] [children].
 *
 * <p>This is to give many outputs using a single command
 *
 * @param <C> the type of context used to run the commands
 * @param <T> the type of commands that can be registered as children in this parent
 */
public interface EasyParentCommand<C extends EasyCommandContext, T extends EasyCommand<C>>
    extends EasyCommand<C> {

  /**
   * Add a children that can be used to run in this parent
   *
   * @param command the child command to add
   */
  default void addCommand(@NonNull T command) {
    this.getChildren().add(command);
  }

  /**
   * Get a children command by an alias
   *
   * @param alias the alias to match the command
   * @return the command if one has the alias, null otherwise
   */
  default T getCommand(@NonNull String alias) {
    for (T child : this.getChildren()) {
      if (child.hasAlias(alias)) return child;
    }
    return null;
  }

  /**
   * Get the collection of registered children in this parent. All the children added in this
   * collection add from {@link #addCommand(EasyCommand)}
   *
   * @return the collection of children
   */
  @NonNull
  Collection<T> getChildren();
}
