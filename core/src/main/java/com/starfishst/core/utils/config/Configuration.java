package com.starfishst.core.utils.config;

import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/** A type of configuration for user input to make changes in the program */
public interface Configuration {

  /**
   * Saves the configuration into the file
   *
   * @throws IOException in case something goes wrong
   */
  void save() throws IOException;

  /**
   * Get the file that the configuration is using
   *
   * @return the file that the configuration is using
   */
  @NotNull
  File getFile();
}
