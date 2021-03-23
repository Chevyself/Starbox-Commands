package me.googas.commands.jda;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.EasyCommand;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.permissions.EasyPermission;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.time.Time;
import net.dv8tion.jda.api.entities.User;

public abstract class EasyJdaCommand implements EasyCommand<CommandContext, EasyJdaCommand> {

  @NonNull protected final CommandManager manager;
  @NonNull @Getter private final Time cooldown;
  @NonNull @Getter private final Set<CooldownUser> cooldownUsers = new HashSet<>();
  @Getter @Setter private EasyPermission permission;
  @Getter @Setter private boolean excluded;

  public EasyJdaCommand(
      @NonNull CommandManager manager,
      EasyPermission permission,
      boolean excluded,
      @NonNull Time cooldown) {
    this.manager = manager;
    this.permission = permission;
    this.excluded = excluded;
    this.cooldown = cooldown;
  }

  public EasyJdaCommand(@NonNull CommandManager manager, boolean excluded, @NonNull Time cooldown) {
    this(manager, null, excluded, cooldown);
  }

  /**
   * Check the cooldown of the sender
   *
   * @param sender the sender of the command
   * @param context the context of the command
   * @return an usage error if the sender is not allowed to use the command yet else null
   */
  public Result checkCooldown(@NonNull User sender, CommandContext context) {
    if (cooldown.toMillis() > 0) {
      CooldownUser cooldownUser = getCooldownUser(sender);
      // TODO make them ignore if the user has certain permission
      if (cooldownUser != null && !cooldownUser.isExpired()) {
        return new Result(
            ResultType.USAGE,
            this.manager.getMessagesProvider().cooldown(cooldownUser.getTimeLeft(), context));
      }
    }
    return null;
  }

  /**
   * Get a cooldown user
   *
   * @param sender the sender to check the cooldown
   * @return a cooldown user if it exists
   */
  public CooldownUser getCooldownUser(@NonNull User sender) {
    for (CooldownUser user : this.cooldownUsers) {
      if (user.getId() == sender.getIdLong()) return user;
    }
    return null;
  }

  @NonNull
  public String getName() {
    return getAliases().get(0);
  }

  @NonNull
  public abstract List<String> getAliases();

  @Override
  public abstract Result execute(@NonNull CommandContext context);

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }
}
