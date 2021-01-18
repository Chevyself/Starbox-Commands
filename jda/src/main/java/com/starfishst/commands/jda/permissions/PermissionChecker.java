package com.starfishst.commands.jda.permissions;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.messages.MessagesProvider;
import com.starfishst.commands.jda.result.Result;
import com.starfishst.commands.jda.result.ResultType;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/** Checks for the permissions of an user when executing a command */
public interface PermissionChecker {

  /**
   * Check the permissions of the command sender. If the result is null the rest of the command will
   * be executed else the given result will be returned.
   *
   * @param context the context of the command
   * @param perm the permission to check
   * @return this default checker will return null if the permission is not {@link
   *     Permission#UNKNOWN} neither empty and if the {@link Permission} is not {@link
   *     Permission#UNKNOWN} it will check the context if it is not a guild context the permission
   *     cannot be checked therefor a result with an error will be given else depending on the
   *     {@link net.dv8tion.jda.api.entities.Member#hasPermission(Permission...)} a result will be
   *     given
   */
  default Result checkPermission(@NonNull CommandContext context, @NonNull JdaPermission perm) {
    Permission permission = perm.getPermission();
    if (permission != Permission.UNKNOWN && context.getMessage().getMember() != null) {
      if (!context.getMessage().getMember().hasPermission(permission)) {
        return new Result(ResultType.PERMISSION, this.getMessagesProvider().notAllowed(context));
      }
    } else if (permission != Permission.UNKNOWN && context.getMessage().getMember() == null) {
      return new Result(ResultType.ERROR, this.getMessagesProvider().guildOnly(context));
    }
    return null;
  }

  /**
   * Get the messages provider in case that the {@link #checkPermission(CommandContext,
   * JdaPermission)} has to return a result with a message.
   *
   * @return the messages provider
   */
  @NonNull
  MessagesProvider getMessagesProvider();
}
