package me.googas.commands.jda.utils.message;

import java.lang.ref.SoftReference;
import java.util.Objects;
import lombok.NonNull;
import lombok.experimental.Delegate;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class FakeMessage implements Message {

  @NonNull private final User user;

  private final Member member;

  @NonNull @Delegate private final SoftReference<Message> message;

  public FakeMessage(@NonNull User user, Member member, @NonNull Message message) {
    this.user = user;
    this.member = member;
    this.message = new SoftReference<>(message);
  }

  @NonNull
  @Delegate(excludes = Author.class)
  public Message validated() {
    return Objects.requireNonNull(this.message.get(), "Message reference has expired");
  }

  @Override
  @NonNull
  public User getAuthor() {
    return this.user;
  }

  @Override
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
