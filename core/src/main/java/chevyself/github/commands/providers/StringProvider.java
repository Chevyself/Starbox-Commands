package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
import chevyself.github.commands.providers.type.StarboxMultipleArgumentProvider;
import chevyself.github.commands.util.Strings;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with {@link String}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class StringProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<String, T>, StarboxMultipleArgumentProvider<String, T> {

  @Override
  public @NonNull Class<String> getClazz() {
    return String.class;
  }

  @NonNull
  @Override
  public String fromString(@NonNull String string, @NonNull T context) {
    return string;
  }

  @Override
  public @NonNull String fromStrings(@NonNull String[] strings, @NonNull T context) {
    return Strings.join(strings);
  }
}
