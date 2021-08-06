package me.googas.commands.jda.permissions;

import lombok.NonNull;
import me.googas.commands.jda.annotations.Command;
import net.dv8tion.jda.api.Permission;

/**
 * This object represents a permission which is created with {@link Command#node()} and {@link
 * Command#permission()}.
 */
public class SimplePermission implements EasyPermission {

  @NonNull private final String node;
  @NonNull private final Permission permission;

  /**
   * Create the permission.
   *
   * @param node the node of the permission
   * @param permission the discord permission
   */
  public SimplePermission(@NonNull String node, @NonNull Permission permission) {
    this.node = node;
    this.permission = permission;
  }

  @Override
  public @NonNull String getNode() {
    return this.node;
  }

  @Override
  public @NonNull Permission getPermission() {
    return this.permission;
  }
}
