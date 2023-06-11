package com.github.chevyself.starbox.system.middleware;

import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.InternalExceptionResult;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext>
    implements SystemMiddleware {

  @Override
  public void onException(
      @NonNull CommandContext context, @NonNull InternalExceptionResult result) {
    result.getException().printStackTrace();
  }

  @Override
  public void onException(
      @NonNull CommandContext context, @NonNull ArgumentExceptionResult result) {
    System.out.println(result.getMessage());
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    System.out.println(result.getMessage());
  }
}
