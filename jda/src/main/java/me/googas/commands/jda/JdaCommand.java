package me.googas.commands.jda;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.StarboxCommand;
import me.googas.commands.flags.Option;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.cooldown.CooldownManager;
import me.googas.commands.jda.result.JdaResult;
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
  @NonNull @Getter protected final String description;
  @NonNull @Getter protected final Map<String, String> map;
  @NonNull @Getter protected final List<Option> options;
  @NonNull @Getter protected final List<Middleware<CommandContext>> middlewares;
  protected final CooldownManager cooldown;

  /**
   * Construct the command.
   *
   * @param manager the manager in which the command is going to be registered
   * @param description a short description of the command
   * @param map the map which contains details of the command
   * @param options the list of options for the command execution
   * @param middlewares the list of middlewares to execute before/after the command
   * @param cooldown the cooldown manager of the command
   */
  public JdaCommand(
      @NonNull CommandManager manager,
      @NonNull String description,
      @NonNull Map<String, String> map,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      CooldownManager cooldown) {
    this.manager = manager;
    this.description = description;
    this.map = map;
    this.options = options;
    this.middlewares = middlewares;
    this.cooldown = cooldown;
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
  abstract JdaResult run(@NonNull CommandContext context);

  @Override
  public JdaResult execute(@NonNull CommandContext context) {
    String[] strings = context.getStrings();
    if (strings.length >= 1) {
      Optional<JdaCommand> optionalCommand = this.getChildren(strings[0]);
      if (optionalCommand.isPresent()) {
        JdaCommand command = optionalCommand.get();
        return command.execute(context.getChildren());
      }
    }
    return this.getMiddlewares().stream()
        .map(middleware -> middleware.next(context))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .map(
            starboxResult -> {
              // Here maybe thrown an error because the wrong result was provided
              return starboxResult instanceof JdaResult ? (JdaResult) starboxResult : null;
            })
        .orElseGet(
            () -> {
              JdaResult run = this.run(context);
              this.getMiddlewares().forEach(middleware -> middleware.next(context, run));
              return run;
            });
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
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
  public @NonNull Optional<CooldownManager> getCooldownManager() {
    return Optional.ofNullable(this.cooldown);
  }
}
