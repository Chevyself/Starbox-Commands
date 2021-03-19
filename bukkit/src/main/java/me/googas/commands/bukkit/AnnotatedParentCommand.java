package me.googas.commands.bukkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyParentCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** TODO documentation */
public class AnnotatedParentCommand extends AnnotatedCommand
    implements EasyParentCommand<CommandContext, BukkitCommand> {

  @NonNull @Getter private final List<BukkitCommand> children;

  public AnnotatedParentCommand(
      @NonNull Command command,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull CommandManager manager,
      @NonNull List<BukkitCommand> children) {
    super(command, method, object, arguments, manager);
    this.children = children;
  }

  /**
   * TODO documentation
   *
   * @return
   */
  @NonNull
  private List<String> getChildrenNames() {
    List<String> names = new ArrayList<>();
    for (BukkitCommand child : this.getChildren()) {
      if (child != null) {
        names.add(child.getName());
      }
    }
    return names;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender commandSender,
      @NonNull String s,
      @NonNull String @NonNull [] strings) {
    if (strings.length >= 1) {
      BukkitCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.execute(commandSender, s, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.execute(commandSender, s, strings);
      }
    } else {
      return super.execute(commandSender, s, strings);
    }
  }

  @NonNull
  @Override
  public List<String> tabComplete(
      @NonNull final CommandSender sender,
      @NonNull final String alias,
      @NonNull final String @NonNull [] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.getChildrenNames(), new ArrayList<>());
    } else if (strings.length >= 2) {
      final BukkitCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      }
    } else {
      return new ArrayList<>();
    }
  }
}
