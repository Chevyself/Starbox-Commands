package chevyself.github.commands.jda.result;

import chevyself.github.commands.jda.AnnotatedCommand;

/**
 * This class allows to implement a builder for {@link JdaResult} which can be used in a {@link
 * AnnotatedCommand}/
 */
public interface JdaResultBuilder {

  /**
   * Build the result.
   *
   * @return the result or null
   */
  JdaResult build();
}
