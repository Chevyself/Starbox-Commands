package me.googas.commands.arguments;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument is just like a {@link SingleArgument} but it has many places in a command which
 * means that multiple inputs are allowed:
 *
 * <p>[prefix][command] [argument 1] [argument 1] [argument 1] [argument 2]
 *
 * <p>[prefix][command] [argument 1] [argument 2] [argument 2] [argument 2]
 *
 * <p>One example could be an array of roles to give an user in Discord
 *
 * @param <O> the type of the class that the argument has to supply
 */
public class MultipleArgument<O> extends SingleArgument<O> {

  @Getter private final int minSize;
  @Getter private final int maxSize;

  /**
   * Get a new instance of the multiple argument
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param clazz the class of the argument
   * @param required is the argument required
   * @param position the position of the argument in the command
   * @param minSize minimum size of the strings that the argument accepts
   * @param maxSize maximum size of strings that the argument accepts
   */
  public MultipleArgument(
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions,
      @NonNull Class<O> clazz,
      boolean required,
      int position,
      int minSize,
      int maxSize) {
    super(name, description, suggestions, clazz, required, position);
    this.minSize = minSize;
    this.maxSize = maxSize;
  }

  @Override
  public String toString() {
    return "MultipleArgument{"
        + "minSize="
        + this.minSize
        + ", maxSize="
        + this.maxSize
        + "} "
        + super.toString();
  }
}
