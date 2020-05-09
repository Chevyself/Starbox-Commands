package com.starfishst.core.arguments;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Is an arguments that requires multiple string to function
 *
 * @param <O> the type that requires the argument
 */
public class MultipleArgument<O> extends Argument<O> {

  /**
   * Get a new instance of {@link MultipleArgument}
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
   * @param position the position of the argument in the command
   */
  public MultipleArgument(
      @NotNull String name,
      @NotNull String description,
      @NotNull List<String> suggestions,
      @NotNull Class<O> clazz,
      boolean required,
      int position) {
    super(name, description, suggestions, clazz, required, position);
  }
}
