package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ArgumentsMap;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.ArgumentExceptionResult;
import com.github.chevyself.starbox.util.Pair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.NonNull;

public class BuiltAbstractCommand<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> extends AbstractCommand<C, T> implements ArgumentedStarboxCommand<C, T> {

  @NonNull
  private final BiFunction<C, ArgumentsMap, Result> executor;
  @NonNull
  @Getter
  private final List<Argument<?>> arguments;

  protected BuiltAbstractCommand(@NonNull CommandBuilder<C, T> built) {
    super(built.getCommandManager(), built.getAliases(), built.getCommandManager().getMiddlewareRegistry()
        .getMiddlewares(built.getExclude(), built.getInclude()), built.getOptions(), built.getMetadata(), built.getChildren());
    this.executor = built.getExecutor();
    this.arguments = built.getArguments();
  }

  @Override
  public Result run(@NonNull C context) {
    try {
      return this.executor.apply(context, BuiltAbstractCommand.getMap(context, this));
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new ArgumentExceptionResult(e);
    }
  }


  @NonNull
  public static <C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> ArgumentsMap getMap(@NonNull C context,
      ArgumentedStarboxCommand<C, T> command)
      throws MissingArgumentException, ArgumentProviderException {
    List<Argument<?>> arguments = command.getArguments();
    Map<String, Object> map = new LinkedHashMap<>(arguments.size());
    List<Object> extra = new ArrayList<>();
    for (Argument<?> argument : arguments) {
      Pair<Object, Integer> pair = argument
          .process(command.getProvidersRegistry(), command.getMessagesProvider(), context,
              0);
      if (argument instanceof SingleArgument) {
        SingleArgument<?> singleArgument = (SingleArgument<?>) argument;
        map.put(singleArgument.getName(), pair.getA());
      } else {
        extra.add(pair.getA());
      }
    }
    return new ArgumentsMap(map, extra);
  }
}
