package com.starfishst.commands.jda.utils.responsive.command;

import com.starfishst.commands.jda.AnnotatedCommand;
import com.starfishst.commands.jda.CommandManager;
import com.starfishst.commands.jda.context.GuildCommandContext;
import com.starfishst.commands.jda.result.Result;
import com.starfishst.commands.jda.utils.embeds.EmbedFactory;
import com.starfishst.commands.jda.utils.message.FakeMessage;
import com.starfishst.commands.jda.utils.responsive.ReactionResponse;
import lombok.NonNull;
import me.googas.commons.Validate;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface CommandReactionResponseMessage extends ReactionResponse {

  /**
   * Get the name of the command to execute
   *
   * @return the name of the command to execute
   */
  @NonNull
  String getCommandName();

  /**
   * Get the arguments to use in the execution of the command
   *
   * @return the arguments
   */
  @NonNull
  String[] getArguments();

  @NonNull
  CommandManager getCommandManager();

  @NonNull
  default AnnotatedCommand getCommand() {
    return Validate.notNull(
        getCommandManager().getCommand(this.getCommandName()),
        "The command " + this.getCommandName() + " seems to not be registered");
  }

  @Override
  default boolean onReaction(@NonNull MessageReactionAddEvent event) {
    if (event.getUser() == null) return true;
    Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
    CommandManager manager = this.getCommandManager();
    String name = this.getCommandName();
    GuildCommandContext context =
        new GuildCommandContext(
            new FakeMessage(event.getUser(), event.getMember(), message),
            event.getUser(),
            this.getArguments(),
            event.getChannel(),
            null,
            manager.getMessagesProvider(),
            manager.getRegistry(),
            name);
    Result result = this.getCommand().execute(context);
    if (result != null) {
      EmbedFactory.fromResult(result, manager.getListener(), context)
          .send(context, msg -> manager.getManagerOptions().getErrorDeleteConsumer());
    }
    return false;
  }
}
