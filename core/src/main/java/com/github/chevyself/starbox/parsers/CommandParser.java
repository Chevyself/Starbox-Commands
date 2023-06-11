package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.CommandCollection;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.annotations.ParentOverride;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderRegistrationException;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.exceptions.MiddlewareParsingException;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.providers.type.StarboxContextualProvider;
import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.util.ClassFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

public abstract class CommandParser<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull @Getter protected final CommandManager<C, T> commandManager;

  public CommandParser(@NonNull CommandManager<C, T> commandManager) {
    this.commandManager = commandManager;
  }

  /**
   * Parse the {@link ReflectCommand} from the provided object. This depends on each implementation
   * of the command manager.
   *
   * @param object the object to get the commands from
   * @return the collection of parsed commands.
   */
  @NonNull
  public List<T> parseAllCommandsFrom(@NonNull Object object) {
    final Class<?> clazz = object.getClass();
    final List<T> commands = new ArrayList<>();
    if (clazz.isAnnotationPresent(Command.class)) {
      commands.add(this.parseAsParentCommand(object, clazz));
    } else {
      commands.addAll(this.parseCommandsCollection(object, clazz));
    }
    return commands;
  }

  @NonNull
  private List<T> parseCommandsCollection(@NonNull Object object, @NonNull Class<?> clazz) {
    final List<T> commands = new ArrayList<>();
    final T parent = this.getAnnotatedParent(object, clazz);
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        final T command = this.parseMethodCommand(object, method);
        if (parent != null) {
          parent.addChild(command);
        } else {
          commands.add(command);
        }
      }
    }
    if (parent != null) {
      commands.add(parent);
    }
    return commands;
  }

  /**
   * Get the parent command from the provided object. This will check for methods with the {@link
   * Parent} annotation
   *
   * @param object the object to get the parent command from
   * @param clazz the class of the object
   * @return the parent command
   */
  private T getAnnotatedParent(@NonNull Object object, @NonNull Class<?> clazz) {
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        return this.parseMethodCommand(object, method);
      }
    }
    return null;
  }

  /**
   * Parse a reflective command using the method where it will be executed and the method instance
   * that must be used to execute the method.
   *
   * @param object the object instance required for the command execution
   * @param method the method used to execute the command
   * @return the parsed command
   */
  @NonNull
  private T parseMethodCommand(@NonNull Object object, @NonNull Method method) {
    this.checkReturnType(method);
    if (!method.isAnnotationPresent(Command.class)) {
      throw new CommandRegistrationException(
          "The method " + method.getName() + " is not annotated with @Command");
    }
    return this.parseCommand(object, method, method.getAnnotation(Command.class));
  }

  /**
   * Registers all the commands in the provided package. This will loop around each class that is
   * annotated with either the command annotation of the module or {@link
   * com.github.chevyself.starbox.annotations.CommandCollection}.
   *
   * <ul>
   *   <li>If the class is annotated with {@link
   *       com.github.chevyself.starbox.annotations.CommandCollection}, then the method {@link
   *       #parseAllCommandsFrom(Object)} will be called to get the commands from the object
   *       instance.
   *   <li>If the class is annotated with the command annotation of the module, then a parent
   *       command will be created: if the class contains a method with the annotation {@link
   *       com.github.chevyself.starbox.annotations.ParentOverride} the default parent command logic
   *       will be overridden, this method is treated as any other command method. If there's no
   *       method with such annotation, then a message with the usage of the subcommands will be
   *       sent.
   * </ul>
   *
   * @param packageName the package name to get the commands from
   * @return this same instance
   */
  @NonNull
  public List<T> parseAllCommandsIn(@NonNull String packageName) {
    List<T> commands = new ArrayList<>();
    this.createClassFinder(null, packageName)
        .setPredicate(ClassFinder.checkForAnyAnnotations(Command.class, CommandCollection.class))
        .find()
        .forEach(
            clazz -> {
              Object instance;
              try {
                Constructor<?> constructor = clazz.getConstructor();
                instance = constructor.newInstance();
              } catch (InstantiationException
                  | IllegalAccessException
                  | InvocationTargetException e) {
                throw new CommandRegistrationException(
                    "Could not instantiate class " + clazz.getName(), e);
              } catch (NoSuchMethodException e) {
                throw new CommandRegistrationException(
                    "Could not find a default constructor in class " + clazz.getName(), e);
              }
              commands.addAll(this.parseAllCommandsFrom(instance));
            });
    return commands;
  }

  /**
   * Creates a class finder with the context of this parser.
   *
   * @param clazz the class of the objects to find
   * @param packageName the package name to get the objects from
   * @return the class finder
   * @param <O> the type of the objects to find
   */
  @NonNull
  public <O> ClassFinder<O> createClassFinder(Class<O> clazz, @NonNull String packageName) {
    return new ClassFinder<>(clazz, packageName).setRecursive(true);
  }

  /**
   * Parses a parent command from a class that is annotated with the command annotation of the
   * module. If no override is provided by {@link #getParentOverride(Class)}, then a default parent
   * command will be created from {@link #getParentCommandSupplier()}
   *
   * @param instance the instance of the class
   * @param clazz the class
   * @return the parent command
   */
  @NonNull
  private T parseAsParentCommand(@NonNull Object instance, @NonNull Class<?> clazz) {
    Command annotation = clazz.getAnnotation(Command.class);
    Optional<Method> override = this.getParentOverride(clazz);
    List<T> children = this.parseCommandsCollection(instance, clazz);
    T parent =
        override
            .map(method -> this.parseCommand(instance, method, annotation))
            .orElseGet(() -> this.getParentCommandSupplier().apply(annotation));
    children.forEach(parent::addChild);
    return parent;
  }

  /**
   * Get the function that will be used to create the default parent command. This will be used if
   * no {@link ParentOverride} is provided in the class.
   *
   * @return the function that will be used to create the default parent command
   */
  @NonNull
  public abstract Function<Command, T> getParentCommandSupplier();

  /**
   * Get the method that overrides the default parent command logic.
   *
   * <p>The overriding method must be annotated with {@link ParentOverride}
   *
   * @param clazz the class to get the method from
   * @return the method that overrides the default parent command logic
   */
  @NonNull
  private Optional<Method> getParentOverride(@NonNull Class<?> clazz) {
    Method optional = null;
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(ParentOverride.class)) {
        optional = method;
        break;
      }
    }
    return Optional.ofNullable(optional);
  }

  /**
   * Check if the return type of the command method is valid. This will throw a {@link
   * CommandRegistrationException} if the return type is not valid. Each module has its own
   * extension of {@link com.github.chevyself.starbox.result.StarboxResult} which can be used as the
   * return type of the command method, you could also use {@link Void} as the return type.
   *
   * @param method the method to check
   */
  public void checkReturnType(@NonNull Method method) {
    Class<?> returnType = method.getReturnType();
    if (!returnType.equals(Void.TYPE) && !StarboxResult.class.isAssignableFrom(returnType)) {
      throw new CommandRegistrationException(
          "The method "
              + method.getName()
              + " has an invalid return type, it must be Void or a subclass of StarboxResult");
    }
  }

  /**
   * Parse the reflection command implementation from the provided object, method and annotation.
   *
   * @param object the object instance required for the command execution
   * @param method the method used to execute the command
   * @param annotation the annotation of the method
   * @return the parsed command
   */
  @NonNull
  public abstract T parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation);

  /**
   * Parse the middlewares in the package and return them as a list.
   *
   * @param packageName the package name to get the middlewares from
   * @return the list of middlewares
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @NonNull
  public List<Middleware<C>> parseAllMiddlewaresIn(@NonNull String packageName) {
    return this.createClassFinder(Middleware.class, packageName).find().stream()
        .map(
            clazz -> {
              if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) return null;
              try {
                Constructor<Middleware> constructor = clazz.getConstructor();
                return (Middleware<C>) constructor.newInstance();
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

  /**
   * Parse the providers in the package and return them as a list. Please note that this method is
   * experimental as it depends on raw types and unchecked casts
   *
   * @param packageName the package name to get the middlewares from
   * @return the list of providers
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @NonNull
  public List<StarboxContextualProvider<?, C>> parseAllProvidersIn(@NonNull String packageName) {
    return this.createClassFinder(StarboxContextualProvider.class, packageName).find().stream()
        .map(
            clazz -> {
              try {
                Constructor<StarboxContextualProvider> constructor = clazz.getConstructor();
                return (StarboxContextualProvider<?, C>) constructor.newInstance();
              } catch (NoSuchMethodException e) {
                throw new ArgumentProviderRegistrationException(
                    "The provider " + clazz.getName() + " must have a default constructor");
              } catch (InvocationTargetException
                  | InstantiationException
                  | IllegalAccessException e) {
                throw new ArgumentProviderRegistrationException(
                    "Could not instantiate the provider " + clazz.getName(), e);
              }
            })
        .collect(Collectors.toList());
  }
}
