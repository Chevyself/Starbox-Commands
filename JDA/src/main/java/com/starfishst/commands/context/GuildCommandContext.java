package com.starfishst.commands.context;

import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This context is used when the command is executed inside of a guild the context is still a {@link
 * User} but you can also get the {@link Member}
 */
public class GuildCommandContext extends CommandContext {

  /** The sender of the command as a member */
  @NotNull private final Member member;
  /** The guild where the command was executed */
  @NotNull private final Guild guild;

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
      @NotNull Message message,
      @NotNull User sender,
      @NotNull String[] args,
      @NotNull MessageChannel channel,
      @Nullable MessageReceivedEvent event,
      @NotNull MessagesProvider messagesProvider,
      ProvidersRegistry<CommandContext> registry,
      String commandName) {
    super(message, sender, args, channel, event, messagesProvider, registry, commandName);
    member =
        Objects.requireNonNull(
            message.getMember(), "Guild command context must have a valid member");
    guild = message.getGuild();
  }

  /**
   * Get the sender of the command as a member
   *
   * @return the sender of the command
   */
  @NotNull
  public Member getMember() {
    return member;
  }

  /**
   * Get the guild where the command was executed
   *
   * @return the guild
   */
  @NotNull
  public Guild getGuild() {
    return guild;
  }

  @Override
  public String toString() {
    return "GuildCommandContext{" + "member=" + member + ", guild=" + guild + '}';
  }
}
