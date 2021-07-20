package me.googas.commands.jda;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.StarboxCommand;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GuildCommandContext;
import me.googas.commands.jda.permissions.EasyPermission;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.starbox.time.Time;
import net.dv8tion.jda.api.entities.User;

/**
 * This is the direct implementation of {@link StarboxCommand} for the "JDA" module extending this
 * class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(JdaCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link AnnotatedCommand}
 *
 * <p>To parse {@link AnnotatedCommand} is required to use the annotation {@link
 * me.googas.commands.jda.annotations.Command} if you would like to create an extension the method
 * to override is {@link #execute(CommandContext)}
 */
public abstract class JdaCommand implements StarboxCommand<CommandContext, JdaCommand> {

  @NonNull protected final CommandManager manager;
  @NonNull @Getter private final Time cooldown;
  @NonNull @Getter private final Set<CooldownUser> cooldownUsers = new HashSet<>();
  @Getter @Setter private EasyPermission permission;
  @Getter @Setter private boolean excluded;

  /**
   * Create the command
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param permission the permission that the sender requires to execute the command {@link
   *     me.googas.commands.jda.permissions.PermissionChecker#checkPermission(CommandContext,
   *     EasyPermission)}
   * @param excluded whether to exclude the {@link Result} of the command from being deleted when it
   *     is {@link ResultType#GENERIC}
   * @param cooldown the time that users must wait until they can use the command again {@link
   *     #checkCooldown(User, CommandContext)}
   */
  public JdaCommand(
      @NonNull CommandManager manager,
      EasyPermission permission,
      boolean excluded,
      @NonNull Time cooldown) {
    this.manager = manager;
    this.permission = permission;
    this.excluded = excluded;
    this.cooldown = cooldown;
  }

  /**
   * Create the command
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param excluded whether to exclude the {@link Result} of the command from being deleted when it
   *     is {@link ResultType#GENERIC}
   * @param cooldown the time that users must wait until they can use the command again {@link
   *     #checkCooldown(User, CommandContext)}
   */
  public JdaCommand(@NonNull CommandManager manager, boolean excluded, @NonNull Time cooldown) {
    this(manager, null, excluded, cooldown);
  }

  /**
   * Check the cooldown of the sender. If there's still an instance of {@link CooldownUser} in
   * {@link #cooldownUsers} a {@link Result} will be returned therefore the command will not be
   * executed.
   *
   * @param sender the sender of the command to check
   * @param context the context of the command
   * @return a {@link ResultType#ERROR} if the sender is not allowed to use the command yet else
   *     null
   */
  public Result checkCooldown(@NonNull User sender, CommandContext context) {
    if (this.cooldown.toMillis() > 0) {
      CooldownUser cooldownUser = this.getCooldownUser(sender);
      // TODO make them ignore if the user has certain permission
      if (cooldownUser != null && !cooldownUser.isExpired()) {
        return new Result(
            ResultType.USAGE,
            this.manager.getMessagesProvider().cooldown(cooldownUser.getTimeLeftMillis(), context));
      }
    }
    return null;
  }

  /**
   * Get an instance of cooldown user. If the user is still in cooldown an instance of {@link
   * CooldownUser} will be returned else null
   *
   * @param sender the sender to get the {@link CooldownUser} if it is on cooldown
   * @return an instance if the sender is in cooldown
   */
  public CooldownUser getCooldownUser(@NonNull User sender) {
    for (CooldownUser user : this.cooldownUsers) {
      if (user.getId() == sender.getIdLong()) return user;
    }
    return null;
  }

  /**
   * Get the name of the command. This is used to execute the command as follows:
   *
   * <p>'[prefix][command_name]'
   *
   * @return the name of the command
   */
  @NonNull
  public String getName() {
    return this.getAliases().get(0);
  }

  /**
   * Get the list of aliases of the command. This is used to execute the command as {@link
   * #getName()}
   *
   * <p>'[prefix][command_alias]'
   *
   * @return the list of aliases
   */
  @NonNull
  public abstract List<String> getAliases();

  /**
   * This is called after {@link #execute(CommandContext)} if no children command is executed
   *
   * @param context the context to execute this command
   * @return the result of the command execution
   */
  abstract Result run(@NonNull CommandContext context);

  @Override
  public Result execute(@NonNull CommandContext context) {
    @NonNull String[] strings = context.getStrings();
    if (strings.length >= 1) {
      JdaCommand command = this.getChildren(strings[0]);
      if (command != null) {
        @NonNull String[] copy = Arrays.copyOfRange(strings, 1, strings.length);
        CommandContext childContext;
        if (context instanceof GuildCommandContext) {
          childContext =
              new GuildCommandContext(
                  context.getMessage(),
                  context.getSender(),
                  copy,
                  context.getChannel(),
                  context.getMessagesProvider(),
                  context.getRegistry(),
                  context.getCommandName());
        } else {
          childContext =
              new CommandContext(
                  context.getMessage(),
                  context.getSender(),
                  copy,
                  context.getChannel(),
                  context.getMessagesProvider(),
                  context.getRegistry(),
                  command.getName());
        }
        return command.execute(childContext);
      }
    }
    return this.run(context);
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }
}
