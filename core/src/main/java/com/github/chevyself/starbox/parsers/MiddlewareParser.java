package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.MiddlewareParsingException;
import com.github.chevyself.starbox.util.ClassFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 * Parser for {@link Middleware} to initialize and register in {@link
 * com.github.chevyself.starbox.StarboxCommandManager}. The registration of middlewares using this
 * method is safe as long as all middlewares have the same context as the one in {@link
 * com.github.chevyself.starbox.StarboxCommandManager} as it uses a lot of raw types and unchecked
 * casts. It also requires that the middlewares have a default constructor with no parameters.
 *
 * <p>The use of this registration method may cause {@link ClassCastException} if not properly used.
 *
 * @param <C>
 */
public class MiddlewareParser<C extends StarboxCommandContext> {

  @NonNull @Getter private final ClassFinder<Middleware<C>> classFinder;

  /**
   * Create a new parser for the middlewares in the given package.
   *
   * @param packageName the package to search for the middlewares
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public MiddlewareParser(@NonNull String packageName) {
    this.classFinder =
        (ClassFinder<Middleware<C>>)
            new ClassFinder(Middleware.class, packageName).setRecursive(true);
  }

  /**
   * Parse the middlewares in the package and return them as a list.
   *
   * @return the list of middlewares
   */
  @NonNull
  public List<Middleware<C>> parse() {
    return this.classFinder.find().stream()
        .map(
            clazz -> {
              if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) return null;
              try {
                Constructor<Middleware<C>> constructor = clazz.getConstructor();
                return constructor.newInstance();
              } catch (NoSuchMethodException e) {
                throw new MiddlewareParsingException(
                    "Middleware must have a no-args constructor", e);
              } catch (InvocationTargetException
                  | InstantiationException
                  | IllegalAccessException e) {
                throw new MiddlewareParsingException("Could not instantiate middleware", e);
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
