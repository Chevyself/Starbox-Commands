package chevyself.github.commands.jda.middleware;

import chevyself.github.commands.jda.annotations.Command;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.result.Result;
import chevyself.github.commands.jda.result.ResultType;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

/**
 * Permission middleware. This checks the permission of the sender before executing a command.
 *
 * <p>To use this middleware you must enter in the {@link Command} map the entry with the key
 * 'permission' and as a value it must be the name of a valid permission in {@link Permission} if
 * the command is executed outside a {@link net.dv8tion.jda.api.entities.Guild} an error message
 * will be displayed as it will not run
 */
public class PermissionMiddleware implements JdaMiddleware {

  @NonNull private static final String key = "permission";

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    Permission permission = this.getPermission(context);
    Optional<Member> optional =
        context.getMessage().map(message -> message.isFromGuild() ? message.getMember() : null);
    Result result = null;
    if (permission != Permission.UNKNOWN) {
      if (optional.isPresent()) {
        if (!optional.get().hasPermission(permission)) {
          result =
              Result.forType(ResultType.PERMISSION)
                  .setDescription(context.getMessagesProvider().notAllowed(context))
                  .build();
        }
      } else {
        result =
            Result.forType(ResultType.ERROR)
                .setDescription(context.getMessagesProvider().guildOnly(context))
                .build();
      }
    }
    return Optional.ofNullable(result);
  }

  @NonNull
  private Permission getPermission(@NonNull CommandContext context) {
    Map<String, String> map = context.getCommand().getMap();
    if (map.containsKey(PermissionMiddleware.key)) {
      try {
        return Permission.valueOf(map.get(PermissionMiddleware.key).toUpperCase());
      } catch (IllegalArgumentException ignored) {
      }
    }
    return Permission.UNKNOWN;
  }
}
