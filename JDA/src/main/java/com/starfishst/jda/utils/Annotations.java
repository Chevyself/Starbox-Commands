package com.starfishst.jda.utils;

import com.starfishst.jda.annotations.Command;
import com.starfishst.jda.permissions.SimplePermission;
import lombok.NonNull;

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
