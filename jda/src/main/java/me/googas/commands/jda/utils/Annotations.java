package me.googas.commands.jda.utils;

import lombok.NonNull;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.permissions.SimplePermission;

/** Static utilities for annotations */
public class Annotations {

  /**
   * Get the permission from a command annotation
   *
   * @param command the command annotation to get the permission from
   * @return the permission
   */
  @NonNull
  public static SimplePermission getPermission(@NonNull Command command) {
    return new SimplePermission(command.node(), command.permission());
  }
}
