package com.starfishst.core.arguments;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Is an arguments that requires multiple string to function
 *
 * @param <O> the type that requires the argument
 */
public class MultipleArgument<O> extends Argument<O> {

  /** The minimum size of the strings that the argument accepts */
  private final int minSize;

  /** The maximum size of strings that the argument accepts */
  private final int maxSize;

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
      @NotNull String name,
      @NotNull String description,
      @NotNull List<String> suggestions,
      @NotNull Class<O> clazz,
      boolean required,
      int position,
      int minSize,
      int maxSize) {
    super(name, description, suggestions, clazz, required, position);
    this.minSize = minSize;
    this.maxSize = maxSize;
  }

  /**
   * Get the minimum size that the argument needs
   *
   * @return the minimum size
   */
  public int getMinSize() {
    return minSize;
  }

  /**
   * Get the maximum size that the argument accepts
   *
   * @return the max size of arguments
   */
  public int getMaxSize() {
    return maxSize;
  }
}
