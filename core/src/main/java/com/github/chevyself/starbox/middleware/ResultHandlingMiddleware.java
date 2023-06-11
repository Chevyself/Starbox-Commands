package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.result.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.InternalExceptionResult;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.result.Result;
import lombok.NonNull;

public abstract class ResultHandlingMiddleware<C extends StarboxCommandContext<C, ?>>
    implements Middleware<C> {

  @Override
  public void next(@NonNull C context, Result result) {
    if (result instanceof SimpleResult) {
      this.onSimple(context, (SimpleResult) result);
    } else if (result instanceof InternalExceptionResult) {
      this.onException(context, (InternalExceptionResult) result);
    } else if (result instanceof ArgumentExceptionResult) {
      this.onException(context, (ArgumentExceptionResult) result);
    }
  }

  public abstract void onException(@NonNull C context, @NonNull InternalExceptionResult result);

  public abstract void onException(@NonNull C context, @NonNull ArgumentExceptionResult result);

  public abstract void onSimple(@NonNull C context, @NonNull SimpleResult result);
}
