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

public class MiddlewareRegistry<C extends StarboxCommandContext<C, ?>> {

  @NonNull @Getter private final List<Middleware<C>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<C>> middlewares;

  public MiddlewareRegistry(
      @NonNull List<Middleware<C>> globalMiddlewares, @NonNull List<Middleware<C>> middlewares) {
    this.globalMiddlewares = globalMiddlewares;
    this.middlewares = middlewares;
  }

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

  public @NonNull List<Middleware<C>> getMiddlewares(@NonNull Command annotation) {
    List<Middleware<C>> list = this.getGlobalMiddlewareAndExclude(annotation);
    list.addAll(this.getIncludeMiddleware(annotation));
    return list;
  }

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

  public void close() {
    this.middlewares.forEach(Middleware::close);
    this.globalMiddlewares.forEach(Middleware::close);
    this.globalMiddlewares.clear();
    this.middlewares.clear();
  }
}
