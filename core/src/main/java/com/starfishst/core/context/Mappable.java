package com.starfishst.core.context;

import lombok.NonNull;

/** It's a class that can be added to a help map */
public interface Mappable {

  /**
   * Get the name for help
   *
   * @return the name
   */
  @NonNull
  String getName();

  /**
   * Get the description for help
   *
   * @return the description
   */
  @NonNull
  String getDescription();
}
