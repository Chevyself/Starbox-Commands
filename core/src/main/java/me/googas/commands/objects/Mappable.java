package me.googas.commands.objects;

import lombok.NonNull;

/**
 * This class represents an object which can be identified with a name and given a description. It
 * is currently used for {@link me.googas.commands.arguments.SingleArgument} and the commands in JDA
 */
public interface Mappable {

  /**
   * Get the name of the object.
   *
   * @return the name of the object as an string
   */
  @NonNull
  String getName();

  /**
   * Get the description of the object.
   *
   * @return the description of the object as an string
   */
  @NonNull
  String getDescription();
}
