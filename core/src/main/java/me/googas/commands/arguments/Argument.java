package me.googas.commands.arguments;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ICommand;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.context.Mappable;
import me.googas.commands.context.Suggestible;

/** An argument is a parameter for the {@link ICommand} different results can be given by them */
public class Argument<O> implements Suggestible, ISimpleArgument<O>, Mappable {

  @NonNull private final String name;
  @NonNull private final String description;
  @NonNull private final List<String> suggestions;
  @NonNull private final Class<O> clazz;
  @Getter private final boolean required;
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
