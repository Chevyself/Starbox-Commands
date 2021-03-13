package me.googas.commands.bukkit;

import lombok.Getter;
import lombok.Setter;

/** Some options for the manager to use */
public class CommandManagerOptions {

  /** Whether or not {@link ParentCommand} should add all the aliases in the tab complete */
  @Getter @Setter private boolean includeAliases;

  /**
   * Create an instance
   *
   * @param includeAliases the value for {@link #includeAliases}
   */
  public CommandManagerOptions(boolean includeAliases) {
    this.includeAliases = includeAliases;
  }
}
