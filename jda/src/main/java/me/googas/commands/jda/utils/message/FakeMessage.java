package me.googas.commands.jda.utils.message;

import java.lang.ref.SoftReference;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.annotations.Nullable;
import me.googas.starbox.Validate;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class FakeMessage implements Message {

  @NonNull private final User user;

  @Nullable private final Member member;

  @NonNull @Delegate private final SoftReference<Message> message;

  public FakeMessage(@NonNull User user, @Nullable Member member, @NonNull Message message) {
    this.user = user;
    this.member = member;
    this.message = new SoftReference<>(message);
  }

  @NonNull
  @Delegate(excludes = Author.class)
  public Message validated() {
    return Validate.notNull(message.get(), "Message reference has expired");
  }

  @Override
  @NonNull
  public User getAuthor() {
    return this.user;
  }

  @Override
  @Nullable
  public Member getMember() {
    return this.member;
  }

  private interface Author {
    @NonNull
    User getAuthor();

    @NonNull
    Member getMember();
  }
}
