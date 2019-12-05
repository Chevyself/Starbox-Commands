package com.starfishst.core.context;

import org.jetbrains.annotations.NotNull;

/** It's a class that can be added to a help map */
public interface IMappable {

  /**
   * Get the name for help
   *
   * @return the name
   */
  @NotNull
  String getName();

  /**
   * Get the description for help
   *
   * @return the description
   */
  @NotNull
  String getDescription();
}
