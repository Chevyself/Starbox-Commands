package com.github.chevyself.starbox.velocity.middleware;

import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.result.ComponentResult;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

public class VelocityResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext> {

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    if (result instanceof ComponentResult) {
      for (Component component : ((ComponentResult) result).getComponents()) {
        context.getSender().sendMessage(component);
      }
    } else {
      super.next(context, result);
    }
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    context.getSender().sendMessage(Component.text(result.getMessage()));
  }
}
