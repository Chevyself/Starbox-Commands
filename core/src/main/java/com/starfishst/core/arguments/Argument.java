package com.starfishst.core.arguments;

import com.starfishst.core.ICommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.context.IMappable;
import com.starfishst.core.context.ISuggestible;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** An argument is a parameter for the {@link ICommand} different results can be given by them */
public class Argument implements ISuggestible, ISimpleArgument, IMappable {

  @NotNull private final String name, description;
  @NotNull private final List<String> suggestions;
  @NotNull private final Class<?> clazz;
  private final boolean required;
  private final int position;

  /**
   * Get a new instance of {@link Argument}
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
   * @param position the position of the argument in the command
   */
  public Argument(
      @NotNull String name,
      @NotNull String description,
      @NotNull List<String> suggestions,
      @NotNull Class<?> clazz,
      boolean required,
      int position) {
    this.name = name;
    this.description = description;
    this.suggestions = suggestions;
    this.clazz = clazz;
    this.required = required;
    this.position = position;
  }

  @NotNull
  @Override
  public String getName() {
    return this.name;
  }

  @NotNull
  @Override
  public String getDescription() {
    return this.description;
  }

  @NotNull
  @Override
  public Class<?> getClazz() {
    return this.clazz;
  }

  /**
   * Get if the argument is required
   *
   * @return is the argument required
   */
  public boolean isRequired() {
    return this.required;
  }

  /**
   * Get the position of the argument
   *
   * @return the position of the argument
   */
  public int getPosition() {
    return this.position;
  }

  @Override
  public String toString() {
    return "Argument{"
            + "name='"
            + this.name
            + '\''
            + ", description='"
            + this.description
            + '\''
            + ", suggestions="
            + this.suggestions
            + ", clazz="
            + this.clazz
            + ", required="
            + this.required
            + ", position="
            + this.position
            + '}';
  }

  @Override
  public @NotNull List<String> getSuggestions(@NotNull ICommandContext<?> context) {
    return this.suggestions;
  }
}
