package me.googas.commands.system.context.sender;

import lombok.NonNull;

/**
 * This is the only implementation of {@link CommandSender} to represent the command executor
 * through console. To use the static instance is provided {@link #INSTANCE}
 */
public class ConsoleCommandSender implements CommandSender {

  @NonNull public static final ConsoleCommandSender INSTANCE = new ConsoleCommandSender();

  private ConsoleCommandSender() {}
}
