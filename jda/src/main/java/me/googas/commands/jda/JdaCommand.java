package me.googas.commands.jda;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.StarboxCommand;
import me.googas.commands.StarboxCooldownManager;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.cooldown.CooldownBehaviour;
import me.googas.commands.jda.cooldown.JdaCooldownManager;
import me.googas.commands.jda.cooldown.UnsupportedContextException;
import me.googas.commands.jda.permissions.Permit;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.time.Time;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

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
  @Getter @Setter private Permit permission;
  @Getter @Setter private boolean excluded;

  protected final JdaCooldownManager cooldown;

  /**
   * Create the command.
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param permission the permission that the sender requires to execute the command {@link
   *     me.googas.commands.jda.permissions.PermissionChecker#checkPermission(CommandContext,
   *     Permit)}
   * @param excluded whether to exclude the {@link Result} of the command from being deleted when it
   *     is {@link ResultType#GENERIC}
   * @param behaviour how should cooldown behave
   * @param cooldown the time that the command needs to cooldown
   * @param cooldownPermit the permission which users may have to not have cooldown
   */
  public JdaCommand(
      @NonNull CommandManager manager,
      Permit permission,
      boolean excluded,
      @NonNull CooldownBehaviour behaviour,
      @NonNull Time cooldown,
      Permit cooldownPermit) {
    this.manager = manager;
    this.permission = permission;
    this.excluded = excluded;
    this.cooldown =
        cooldown.toMillisRound() > 0 ? behaviour.create(cooldown, cooldownPermit) : null;
  }

  /**
   * Create the command.
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param excluded whether to exclude the {@link Result} of the command from being deleted when it
   *     is {@link ResultType#GENERIC}
   * @param behaviour how should cooldown behave
   * @param cooldown the time that the command needs to cooldown
   * @param cooldownPermit the permission which users may have to not have cooldown
   */
  public JdaCommand(
      @NonNull CommandManager manager,
      boolean excluded,
      @NonNull CooldownBehaviour behaviour,
      @NonNull Time cooldown,
      Permit cooldownPermit) {
    this(manager, null, excluded, behaviour, cooldown, cooldownPermit);
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
   * This is called after {@link #execute(CommandContext)} if no children command is executed.
   *
   * @param context the context to execute this command
   * @return the result of the command execution
   */
  abstract Result run(@NonNull CommandContext context);

  @Override
  public Result execute(@NonNull CommandContext context) {
    @NonNull String[] strings = context.getStrings();
    if (strings.length >= 1) {
      Optional<JdaCommand> optionalCommand = this.getChildren(strings[0]);
      if (optionalCommand.isPresent()) {
        JdaCommand command = optionalCommand.get();
        return command.execute(context.getChildren());
      }
    }
    Result result =
        this.manager.getPermissionChecker().checkPermission(context, this.getPermission());
    if (result == null) {
      try {
        if (this.cooldown != null && this.cooldown.hasCooldown(context)) {
          result =
              Result.forType(ResultType.USAGE)
                  .setDescription(
                      this.manager
                          .getMessagesProvider()
                          .cooldown(context, this.cooldown.getTimeLeft(context)))
                  .build();
        } else {
          result = this.run(context);
        }
        if (result.isCooldown() && this.cooldown != null) {
          this.cooldown.refresh(context);
        }
      } catch (UnsupportedContextException e) {
        result =
            Result.forType(ResultType.ERROR)
                .setDescription(this.manager.getMessagesProvider().guildOnly(context))
                .build();
      }
    }
    return result;
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }

  /**
   * Get the description of the command.
   *
   * @return the description
   */
  @NonNull
  public String getDescription() {
    return "No description given";
  }

  /**
   * Get the data of the command.
   *
   * @return the command data
   */
  @NonNull
  public SlashCommandData getCommandData() {
    SlashCommandData commandData = Commands.slash(this.getName(), this.getDescription());
    this.getChildren().stream()
        .map(JdaCommand::toSubcommandData)
        .forEach(commandData::addSubcommands);
    return commandData;
  }

  @NonNull
  private SubcommandData toSubcommandData() {
    return new SubcommandData(this.getName(), this.getDescription());
  }

  @Override
  public @NonNull Optional<? extends StarboxCooldownManager<CommandContext>> getCooldownManager() {
    return Optional.ofNullable(this.cooldown);
  }
}
