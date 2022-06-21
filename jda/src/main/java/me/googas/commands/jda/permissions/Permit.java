package me.googas.commands.jda.permissions;

import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.annotations.Perm;
import net.dv8tion.jda.api.Permission;

/** This object represents a permission that a command may have. */
public interface Permit {

  @NonNull
  static Optional<Permit> from(@NonNull Perm permission) {
    if (permission.value() == Permission.UNKNOWN || permission.node().isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(new SimplePermit(permission.node(), permission.value()));
    }
  }

  /**
   * The node of the permission which can be any string.
   *
   * @see Command#node()
   * @return the node as a string
   */
  @NonNull
  String getNode();

  /**
   * The discord permission which can be given from inside the app.
   *
   * @see Command#permission()
   * @return the discord permission
   */
  @NonNull
  Permission getPermission();
}
