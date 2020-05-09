package com.starfishst.bukkit;

/** Some options for the manager to use */
public class CommandManagerOptions {

  /** Whether or not {@link ParentCommand} should add all the aliases in the tab complete */
  private final boolean includeAliases;

  /**
   * Create an instance
   *
   * @param includeAliases the value for {@link #includeAliases}
   */
  public CommandManagerOptions(boolean includeAliases) {
    this.includeAliases = includeAliases;
  }

  /**
   * Get if parent commands should add all the aliases in tab complete
   *
   * @return true if it should add all of them
   */
  public boolean isIncludeAliases() {
    return this.includeAliases;
  }
}
