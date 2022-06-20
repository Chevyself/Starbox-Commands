package me.googas.commands.jda.cooldown;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.time.Time;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MemberCooldownManager extends JdaCooldownManager {

  private final Set<GuildMap> guilds = new HashSet<>();

  protected MemberCooldownManager(@NonNull Time time) {
    super(time);
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    return this.getGuildMap(context).map(guild -> guild.hasCooldown(context)).orElse(false);
  }

  @NonNull
  private Optional<GuildMap> getGuildMap(@NonNull CommandContext context) {
    return guilds.stream().filter(map -> map.getId() == this.getGuildId(context)).findFirst();
  }

  private long getGuildId(@NonNull CommandContext context) {
    Optional<MessageChannel> optional = context.getChannel();
    return context
        .getChannel()
        .map(
            channel -> {
              if (channel instanceof TextChannel) {
                return ((TextChannel) channel).getGuild();
              }
              return null;
            })
        .map(ISnowflake::getIdLong)
        .orElseThrow(UnsupportedContextException::new);
  }

  @Override
  public @NonNull Time getTimeLeft(@NonNull CommandContext context) {
    return this.getGuildMap(context).map(map -> map.getTimeLeft(context)).orElse(Time.ZERO);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    this.getGuildMap(context)
        .orElseGet(
            () -> {
              GuildMap guildMap = new GuildMap(this.time, this.getGuildId(context));
              this.guilds.add(guildMap);
              return guildMap;
            })
        .refresh(context);
  }

  private static class GuildMap extends UserCooldownManager {

    @Getter private final long id;

    protected GuildMap(@NonNull Time time, long id) {
      super(time);
      this.id = id;
    }
  }
}
