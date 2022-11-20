package chevyself.github.commands.annotations;

import chevyself.github.commands.arguments.Argument;
import chevyself.github.commands.arguments.ArgumentBehaviour;
import chevyself.github.commands.arguments.SingleArgument;
import chevyself.github.commands.context.StarboxCommandContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

/**
 * This marks a parameter as required. It can be thought in a way as {@link NonNull}
 *
 * @see Argument
 *     <pre>
 * public void AMethod(@Required String name, @Free(suggestions = "20") int age) {
 *     // A required argument is name
 *     // An optional argument is age
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Required {

  /**
   * Returns the name of the argument.
   *
   * @return the name of the argument
   */
  @NonNull
  String name() default "No name provided";

  /**
   * Returns the description of the argument.
   *
   * @return the description of the argument
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Returns an {@link java.lang.reflect.Array} of strings which represents the suggestions of the
   * argument.
   *
   * @see SingleArgument#getSuggestions(StarboxCommandContext)
   * @return the array of suggestions
   */
  @NonNull
  String[] suggestions() default {};

  /**
   * Returns the behaviour of the argument.
   *
   * @return the behaviour
   */
  ArgumentBehaviour behaviour() default ArgumentBehaviour.NORMAL;
}
