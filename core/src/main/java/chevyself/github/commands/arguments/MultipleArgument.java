package chevyself.github.commands.arguments;

import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.exceptions.MissingArgumentException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import chevyself.github.commands.util.Pair;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument is just like a {@link SingleArgument} but it has many places in a command which
 * means that multiple inputs are allowed.
 *
 * <p>[prefix][command] [argument 1] [argument 1] [argument 1] [argument 2]
 *
 * <p>[prefix][command] [argument 1] [argument 2] [argument 2] [argument 2]
 *
 * <p>One example could be an array of roles to give a user in Discord
 *
 * @param <O> the type of the class that the argument has to supply
 */
@Deprecated
public class MultipleArgument<O> extends SingleArgument<O> {

  @Getter private final int minSize;
  @Getter private final int maxSize;

  /**
   * Get a new instance of the multiple argument.
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
    super(name, description, suggestions, ArgumentBehaviour.MULTIPLE, clazz, required, position);
    this.minSize = minSize;
    this.maxSize = maxSize;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MultipleArgument.class.getSimpleName() + "[", "]")
        .add("minSize=" + minSize)
        .add("maxSize=" + maxSize)
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
    MultipleArgument<?> that = (MultipleArgument<?>) o;
    return minSize == that.minSize && maxSize == that.maxSize;
  }

  @Override
  public <T extends StarboxCommandContext> Pair<Object, Integer> process(
      @NonNull ProvidersRegistry<T> registry,
      @NonNull StarboxMessagesProvider<T> messages,
      @NonNull T context,
      int lastIndex)
      throws ArgumentProviderException, MissingArgumentException {
    String[] strings = context.getStringsFrom(this.getPosition());
    if (strings.length < this.getMinSize() && this.isRequired()) {
      throw new MissingArgumentException(
          messages.missingStrings(
              this.getName(),
              this.getDescription(),
              this.getPosition(),
              this.getMinSize(),
              this.getMinSize() - strings.length,
              context));
    }
    return new Pair<>(registry.fromStrings(strings, this.getClazz(), context), 0);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minSize, maxSize);
  }
}
