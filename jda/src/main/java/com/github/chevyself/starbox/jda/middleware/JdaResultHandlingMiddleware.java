package com.github.chevyself.starbox.jda.middleware;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.SlashCommandContext;
import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.InternalExceptionResult;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.SimpleResult;
import java.util.Optional;
import lombok.NonNull;

public class JdaResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext> {

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    if (context instanceof SlashCommandContext) {
      ((SlashCommandContext) context).getEvent().deferReply(true);
    }
    return Optional.empty();
  }

  public void sendMessage(@NonNull CommandContext context, @NonNull String message) {
    if (context instanceof SlashCommandContext) {
      ((SlashCommandContext) context).getEvent().deferReply(true).setContent(message).queue();
    } else {
      context.getChannel().ifPresent(channel -> channel.sendMessage(message).queue());
    }
  }

  @Override
  public void onException(@NonNull CommandContext context,
      @NonNull InternalExceptionResult result) {
    this.sendMessage(context, result.getMessage());
  }

  @Override
  public void onException(@NonNull CommandContext context,
      @NonNull ArgumentExceptionResult result) {
    this.sendMessage(context, result.getMessage());
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    this.sendMessage(context, result.getMessage());
  }
}
