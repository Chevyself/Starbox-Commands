package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.result.type.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.type.InternalExceptionResult;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import lombok.NonNull;

public abstract class ResultHandlingMiddleware<C extends StarboxCommandContext<C, ?>>
    implements Middleware<C> {

  @Override
  public void next(@NonNull C context, Result result) {
    if (result instanceof SimpleResult) {
      if (result instanceof InternalExceptionResult) {
        ((InternalExceptionResult) result).getException().printStackTrace();
      }
      this.onSimple(context, (SimpleResult) result);
    }
  }

  public abstract void onSimple(@NonNull C context, @NonNull SimpleResult result);
}
