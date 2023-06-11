package com.github.chevyself.starbox.system.middleware;

import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.ExceptionResult;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext> implements
    SystemMiddleware {


  @Override
  public void onException(@NonNull CommandContext context, @NonNull ExceptionResult result) {
    result.getException().printStackTrace();
  }

  @Override
  public void onSimple(@NonNull SimpleResult result) {
    System.out.println(result.getMessage());
  }
}
