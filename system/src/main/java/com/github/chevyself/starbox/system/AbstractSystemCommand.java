package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.util.Strings;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/** Abstract implementation for system commands. */
public abstract class AbstractSystemCommand implements SystemCommand {

  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<SystemCommand> children;
  @NonNull @Getter private final List<Option> options;
  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares;

  private final CooldownManager cooldownManager;

  /**
   * Create the abstract command.
   *
   * @param aliases the aliases of the command
   * @param children the children of the command
   * @param options the options/flags of the command
   * @param middlewares the middlewares of the command
   * @param cooldownManager the cooldown manager of the command
   */
  public AbstractSystemCommand(
      @NonNull List<String> aliases,
      @NonNull List<SystemCommand> children,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      CooldownManager cooldownManager) {
    this.aliases = aliases;
    this.children = children;
    this.options = options;
    this.middlewares = middlewares;
    this.cooldownManager = cooldownManager;
  }

  @Override
  public @NonNull String getName() {
    return this.aliases.get(0);
  }

  @Override
  public boolean hasAlias(@NonNull String string) {
    for (String alias : this.aliases) {
      if (string.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @NonNull Optional<CooldownManager> getCooldownManager() {
    return Optional.ofNullable(this.cooldownManager);
  }

  @Override
  public @NonNull String getUsage() {
    return Strings.buildUsageAliases(this.aliases);
  }
}
