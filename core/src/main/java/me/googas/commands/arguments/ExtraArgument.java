package me.googas.commands.arguments;

import java.lang.annotation.Annotation;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument is not exactly given by the user but by the context of the command execution, that
 * is why it does not require an annotation as you can see in {@link Argument#isEmpty(Annotation[])}
 * if this method returns true it will be considered as an {@link ExtraArgument}
 *
 * <p>The first example of an extra argument is the context of the command execution another two
 * examples could be the location of the Minecraft player that ran the command or the text channel
 * where the command was sent in Discord.
 *
 * @param <O> the type of the class that the argument has to supply
 */
public class ExtraArgument<O> implements Argument<O> {

  @NonNull @Getter private final Class<O> clazz;

  /**
   * Create a new extra argument instance
   *
   * @param clazz the class of the argument
   */
  public ExtraArgument(@NonNull Class<O> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return "ExtraArgument{" + "clazz=" + clazz + '}';
  }
}
