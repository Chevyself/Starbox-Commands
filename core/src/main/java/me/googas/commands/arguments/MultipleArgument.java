package me.googas.commands.arguments;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/**
 * Is an arguments that requires multiple string to function
 *
 * @param <O> the type that requires the argument
 */
public class MultipleArgument<O> extends Argument<O> {

  @Getter private final int minSize;
  @Getter private final int maxSize;

  /**
   * Get a new instance of {@link MultipleArgument}
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
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
}
