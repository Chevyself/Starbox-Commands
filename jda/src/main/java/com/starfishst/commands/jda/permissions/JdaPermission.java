package com.starfishst.commands.jda.permissions;

import com.starfishst.commands.jda.annotations.Command;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/** This object represents a permission that a command may have */
public interface JdaPermission {

  /**
   * The node of the permission which can be any string
   *
   * @see Command#node()
   * @return the node as a string
   */
  @NonNull
  String getNode();

  /**
   * The discord permission which can be given from inside the app
   *
   * @see Command#permission()
   * @return the discord permission
   */
  @NonNull
  Permission getPermission();
}
