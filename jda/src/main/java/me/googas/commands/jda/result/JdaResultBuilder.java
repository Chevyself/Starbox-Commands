package me.googas.commands.jda.result;

/**
 * This class allows to implement a builder for {@link JdaResult} which can be used in a {@link
 * me.googas.commands.jda.AnnotatedCommand}/
 */
public interface JdaResultBuilder {

  /**
   * Build the result.
   *
   * @return the result or null
   */
  JdaResult build();
}
