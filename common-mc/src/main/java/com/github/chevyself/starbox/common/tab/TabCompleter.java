package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.util.List;
import lombok.NonNull;

public interface TabCompleter<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>, O> {

  @NonNull
  List<String> tabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String name, @NonNull String[] strings);
}
