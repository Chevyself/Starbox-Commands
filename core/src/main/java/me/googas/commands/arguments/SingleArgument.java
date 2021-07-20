package me.googas.commands.arguments;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.objects.Mappable;

/**
 * This argument just like a {@link SingleArgument} but it has many places in a command which means
 * that multiple inputs are allowed:
 *
 * <p>[prefix][command] [argument 1] [argument 1] [argument 1] [argument 2]
 *
 * <p>[prefix][command] [argument 1] [argument 2] [argument 2] [argument 2]
 *
 * <p>The best example is the name or the age for an user that is being created
 *
 * @param <O> the type of the class that the argument has to supply
 */
public class SingleArgument<O> implements Argument<O>, Mappable {

  @NonNull @Getter private final String name;
  @NonNull @Getter private final String description;
  @NonNull private final List<String> suggestions;
  @NonNull @Getter private final Class<O> clazz;
  @Getter private final boolean required;
  @Getter private final int position;

  /**
   * Get a new instance of a single argument
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
   * @param position the position of the argument in the command
   */
  public SingleArgument(
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

  /**
   * Get a list of suggestions to successfully use the argument
   *
   * @param context the command context to help use the suggestions
   * @return the list of suggestions
   */
  @NonNull
  public List<String> getSuggestions(@NonNull StarboxCommandContext context) {
    return this.suggestions;
  }

  @Override
  public String toString() {
    return "SingleArgument{"
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
}
