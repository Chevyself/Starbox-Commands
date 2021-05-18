package me.googas.commands.jda.context;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * This context is used when the command is executed inside of a guild the context is still a {@link
 * User} but you can also get the {@link Member}
 */
public class GuildCommandContext extends CommandContext {

  /** The sender of the command as a member */
  @NonNull @Getter private final Member member;
  /** The guild where the command was executed */
  @NonNull @Getter private final Guild guild;

  /**
   * Create an instance
   *
   * @param message the message that executed the command
   * @param sender the sender of the command
   * @param args the strings representing the arguments of the command
   * @param channel the channel where the command was executed
   * @param event the event where the command was executed
   * @param messagesProvider the messages provider for this context
   * @param registry the registry of this context
   * @param commandName the name of the command that is being executed
   */
  public GuildCommandContext(
      @NonNull Message message,
      @NonNull User sender,
      @NonNull String[] args,
      @NonNull MessageChannel channel,
      MessageReceivedEvent event,
      @NonNull MessagesProvider messagesProvider,
      ProvidersRegistry<CommandContext> registry,
      String commandName) {
    super(message, sender, args, channel, messagesProvider, registry, commandName);
    this.member =
        Objects.requireNonNull(
            message.getMember(), "Guild command context must have a valid member");
    this.guild = message.getGuild();
  }

  @Override
  public String toString() {
    return "GuildCommandContext{" + "member=" + this.member + ", guild=" + this.guild + '}';
  }
}
