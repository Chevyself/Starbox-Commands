package me.googas.commands.arguments;

import lombok.NonNull;
import me.googas.commons.builder.ToStringBuilder;

/** An extra argument will be in the command method but it is not needed in the command string */
public class ExtraArgument<O> implements ISimpleArgument<O> {

  @NonNull private final Class<O> clazz;

  /**
   * Get a new instance of {@link ExtraArgument}
   *
   * @param clazz the class of the argument
   */
  public ExtraArgument(@NonNull Class<O> clazz) {
    this.clazz = clazz;
  }

  @NonNull
  @Override
  public Class<O> getClazz() {
    return this.clazz;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("clazz", clazz).build();
  }
}
