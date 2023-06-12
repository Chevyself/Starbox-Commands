package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

public abstract class GenericTabCompleter<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>, O>
    implements TabCompleter<C, T, O> {

  public abstract String getPermission(@NonNull T command);

  public abstract boolean hasPermission(@NonNull O sender, @NonNull String permission);

  @NonNull
  public List<String> tabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String name, @NonNull String[] strings) {
    String permission = this.getPermission(command);
    if (permission != null && !this.hasPermission(sender, permission)) {
      return new ArrayList<>();
    }
    if (strings.length == 1) {
      return Strings.copyPartials(strings[strings.length - 1], command.getChildrenNames());
    } else if (strings.length >= 2) {
      Optional<T> optional = command.getChild(strings[0]);
      return optional
          .map(
              child ->
                  this.tabComplete(
                      child, sender, strings[0], Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(ArrayList::new);
    }
    return new ArrayList<>();
  }
}
