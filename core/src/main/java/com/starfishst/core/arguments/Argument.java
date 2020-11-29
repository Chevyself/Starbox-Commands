package com.starfishst.core.arguments;

import com.starfishst.core.ICommand;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.context.IMappable;
import com.starfishst.core.context.ISuggestible;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/** An argument is a parameter for the {@link ICommand} different results can be given by them */
public class Argument<O> implements ISuggestible, ISimpleArgument<O>, IMappable {

  /** The name of the argument */
  @NonNull private final String name;
  /** The description of the argument */
  @NonNull private final String description;
  /** A list of suggestions for the user that will need to input the argument */
  @NonNull private final List<String> suggestions;
  /** Get the class that represents the argument */
  @NonNull private final Class<O> clazz;
  /** If the argument is required */
  @Getter private final boolean required;
  /** The position of the argument */
  @Getter private final int position;

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
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions,
      @NonNull Class<O> clazz,
      boolean required,
      int position) {
    this.name = name;
    this.description = description;
    this.suggestions = suggestions;
    this.clazz = clazz;
    this.required = required;
    this.position = position;
  }

  @NonNull
  @Override
  public String getName() {
    return this.name;
  }

  @NonNull
  @Override
  public String getDescription() {
    return this.description;
  }

  @NonNull
  @Override
  public Class<O> getClazz() {
    return this.clazz;
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
  public @NonNull List<String> getSuggestions(@NonNull ICommandContext context) {
    return this.suggestions;
  }
}
