package com.github.chevyself.starbox.arguments;

import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.objects.Mappable;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.util.Pair;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument just like a {@link SingleArgument} but it has many places in a command which means
 * that multiple inputs are allowed.
 *
 * <p>[prefix][command] [argument 1] [argument 1] [argument 1] [argument 2]
 *
 * <p>[prefix][command] [argument 1] [argument 2] [argument 2] [argument 2]
 *
 * <p>The best example is the name or the age for a user that is being created
 *
 * @param <O> the type of the class that the argument has to supply
 */
public class SingleArgument<O> implements Argument<O>, Mappable {

  @NonNull @Getter private final String name;
  @NonNull @Getter private final String description;
  @NonNull private final List<String> suggestions;
  @NonNull @Getter private final ArgumentBehaviour behaviour;
  @NonNull @Getter private final Class<O> clazz;
  @Getter private final boolean required;
  @Getter private final int position;

  /**
   * Get a new instance of a single argument.
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param behaviour the behaviour of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
   * @param position the position of the argument in the command
   */
  public SingleArgument(
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions,
      @NonNull ArgumentBehaviour behaviour,
      @NonNull Class<O> clazz,
      boolean required,
      int position) {
    this.name = name;
    this.description = description;
    this.suggestions = suggestions;
    this.clazz = clazz;
    this.behaviour = behaviour;
    this.required = required;
    this.position = position;
  }

  /**
   * Get a list of suggestions to successfully use the argument.
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
    return new StringJoiner(", ", SingleArgument.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("description='" + description + "'")
        .add("suggestions=" + suggestions)
        .add("clazz=" + clazz)
        .add("required=" + required)
        .add("position=" + position)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    SingleArgument<?> that = (SingleArgument<?>) o;
    return required == that.required
        && position == that.position
        && name.equals(that.name)
        && description.equals(that.description)
        && suggestions.equals(that.suggestions)
        && clazz.equals(that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, suggestions, clazz, required, position);
  }

  @Override
  public <T extends StarboxCommandContext<T, ?>> Pair<Object, Integer> process(
      @NonNull ProvidersRegistry<T> registry,
      @NonNull MessagesProvider<T> messages,
      @NonNull T context,
      int lastIndex)
      throws ArgumentProviderException, MissingArgumentException {
    Object object;
    Pair<String, Integer> argumentString = ReflectCommand.getArgument(this, context, lastIndex);
    String string = argumentString.getA();
    if (string == null) {
      if (this.isRequired()) {
        throw new MissingArgumentException(
            messages.missingArgument(
                this.getName(), this.getDescription(), this.getPosition(), context));
      } else {
        object = null;
      }
    } else {
      object = registry.fromString(string, this.getClazz(), context);
    }
    return new Pair<>(object, argumentString.getB());
  }
}
