package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.result.ExceptionResult;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.result.StarboxResult;
import lombok.NonNull;

public abstract class ResultHandlingMiddleware<C extends StarboxCommandContext<C, ?>> implements Middleware<C> {

  @Override
  public void next(@NonNull C context, StarboxResult result) {
    if (result instanceof SimpleResult) {
      this.onSimple((SimpleResult) result);
    } else if (result instanceof ExceptionResult) {
      this.onException(context, (ExceptionResult) result);
    }
  }

  public abstract void onException(@NonNull C context, @NonNull ExceptionResult result);

  public abstract void onSimple(@NonNull SimpleResult result);
}
