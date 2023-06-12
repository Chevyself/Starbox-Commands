package com.github.chevyself.starbox.registry;

import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.middleware.Middleware;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 * Contains the global and command-specific {@link Middleware}. This can be used in
 * the {@link com.github.chevyself.starbox.CommandManager} to be provided in commands
 * using {@link #getMiddlewares(Command)},
 *
 * @param <C> the type of context
 */
public final class MiddlewareRegistry<C extends StarboxCommandContext<C, ?>> {

  @NonNull @Getter private final List<Middleware<C>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<C>> middlewares;

  /**
   * Create the registry.
   *
   * @param globalMiddlewares the global middlewares
   * @param middlewares the specific middlewares
   */
  public MiddlewareRegistry(
      @NonNull List<Middleware<C>> globalMiddlewares, @NonNull List<Middleware<C>> middlewares) {
    this.globalMiddlewares = globalMiddlewares;
    this.middlewares = middlewares;
  }

  /**
   * Create an empty registry.
   */
  public MiddlewareRegistry() {
    this(new ArrayList<>(), new ArrayList<>());
  }

  /**
   * Add a global {@link Middleware} to this manager.
   *
   * @param middleware the global middleware to add
   * @return this same instance
   */
  @NonNull
  public MiddlewareRegistry<C> addGlobalMiddleware(@NonNull Middleware<C> middleware) {
    this.globalMiddlewares.add(middleware);
    return this;
  }

  /**
   * Add a {@link Middleware} to this manager.
   *
   * @param middleware the middleware to add
   * @return this same instance
   */
  @NonNull
  public MiddlewareRegistry<C> addMiddleware(@NonNull Middleware<C> middleware) {
    this.middlewares.add(middleware);
    return this;
  }

  /**
   * Add many global {@link Middleware} to this manager.
   *
   * @param middlewares the array of global middlewares to add
   * @return this same instance
   */
  @SafeVarargs
  @NonNull
  public final MiddlewareRegistry<C> addGlobalMiddlewares(@NonNull Middleware<C>... middlewares) {
    for (Middleware<C> middleware : middlewares) {
      this.addGlobalMiddleware(middleware);
    }
    return this;
  }

  /**
   * Add many {@link Middleware} to this manager.
   *
   * @param middlewares the array of middlewares to add
   * @return this same instance
   */
  @SafeVarargs
  @NonNull
  public final MiddlewareRegistry<C> addMiddlewares(@NonNull Middleware<C>... middlewares) {
    for (Middleware<C> middleware : middlewares) {
      this.addMiddleware(middleware);
    }
    return this;
  }

  /**
   * Get the middlewares for a command. This will get all the global middlewares unless
   * they are excluded in the {@link Command#exclude()} annotation. Then, it will get all the
   * middlewares that are included in the {@link Command#include()} annotation.
   *
   * @param annotation the command annotation
   * @return the list of middlewares
   */
  public @NonNull List<Middleware<C>> getMiddlewares(@NonNull Command annotation) {
    List<Middleware<C>> list = this.getGlobalMiddlewareAndExclude(annotation);
    list.addAll(this.getIncludeMiddleware(annotation));
    return list;
  }

  /**
   * Get all the global middlewares except the ones that are excluded in the {@link Command#exclude()}.
   *
   * @param annotation the command annotation
   * @return the list of middlewares
   */
  private @NonNull List<Middleware<C>> getGlobalMiddlewareAndExclude(Command annotation) {
    return this.globalMiddlewares.stream()
        .filter(
            middleware -> {
              for (Class<? extends Middleware<?>> clazz : annotation.exclude()) {
                if (clazz.isAssignableFrom(middleware.getClass())) {
                  return false;
                }
              }
              return true;
            })
        .collect(Collectors.toList());
  }

  /**
   * Get all the middlewares that are included in the {@link Command#include()}.
   *
   * @param annotation the command annotation
   * @return the list of middlewares
   */
  private @NonNull Collection<? extends Middleware<C>> getIncludeMiddleware(Command annotation) {
    return middlewares.stream()
        .filter(
            middleware -> {
              for (Class<? extends Middleware<?>> clazz : annotation.include()) {
                if (clazz.isAssignableFrom(middleware.getClass())) {
                  return true;
                }
              }
              return false;
            })
        .collect(Collectors.toList());
  }

  /**
   * Closes the registry and all the middlewares.
   */
  public void close() {
    this.middlewares.forEach(Middleware::close);
    this.globalMiddlewares.forEach(Middleware::close);
    this.globalMiddlewares.clear();
    this.middlewares.clear();
  }
}
