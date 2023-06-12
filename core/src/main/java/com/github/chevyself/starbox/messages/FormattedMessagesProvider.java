package com.github.chevyself.starbox.messages;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

public abstract class FormattedMessagesProvider<C extends StarboxCommandContext<C, ?>>
    implements MessagesProvider<C> {

  @NonNull
  public abstract String element();

  @NonNull
  public abstract String text();

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull C context) {
    return String.format("%s%s %sis not a valid long!", this.element(), string, this.text());
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull C context) {
    return String.format("%s%s %sis not a valid integer!", this.element(), string, this.text());
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull C context) {
    return String.format("%s%s %sis not a valid double!", this.element(), string, this.text());
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull C context) {
    return String.format("%s%s %sis not a valid boolean!", this.element(), string, this.text());
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull C context) {
    return String.format("%s%s %sis not valid time!", this.element(), string, this.text());
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name, @NonNull String description, int position, C context) {
    return this.text()
        + "Missing the argument "
        + this.element()
        + name
        + this.text()
        + " ("
        + this.element()
        + description
        + this.text()
        + ") in "
        + this.element()
        + position;
  }

  @Override
  public @NonNull String cooldown(@NonNull C context, @NonNull Duration timeLeft) {
    return String.format(
        "%sYou are not allowed to run this command for another %s%s",
        this.text(), this.element(), TimeUtil.toString(timeLeft));
  }
}
