package com.starfishst.commands;

public class CommandManagerOptions {

  private final boolean includeAliases;

  public CommandManagerOptions(boolean includeAliases) {
    this.includeAliases = includeAliases;
  }

  public boolean isIncludeAliases() {
    return this.includeAliases;
  }
}
