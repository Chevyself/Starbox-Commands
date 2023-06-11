package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractParentCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> {

  public AbstractParentCommand(@NonNull CommandManager<C, T> commandManager,
      @NonNull Command annotation) {
    super(commandManager, annotation);
  }

  @Override
  public StarboxResult run(@NonNull C context) {
    return new SimpleResult(StarboxCommand.genericHelp(this, this.getChildren()));
  }
}
