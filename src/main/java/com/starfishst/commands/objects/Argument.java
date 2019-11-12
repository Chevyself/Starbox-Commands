package com.starfishst.commands.objects;

import org.jetbrains.annotations.NotNull;

public class Argument {

  @NotNull private final Class<?> clazz;
  private final boolean required;
  private final int position;
  @NotNull private final String[] suggestions;
  @NotNull private final String name;
  @NotNull private final String description;

  public Argument(
      @NotNull final Class<?> clazz,
      final boolean required,
      final int position,
      @NotNull final String[] suggestions,
      @NotNull String name,
      @NotNull String description) {
    this.clazz = clazz;
    this.required = required;
    this.position = position;
    this.suggestions = suggestions;
    this.name = name;
    this.description = description;
  }

  public Argument(
      @NotNull Class<?> clazz, boolean required, int position, @NotNull String[] suggestions) {
    this(clazz, required, position, suggestions, "", "");
  }

  @NotNull
  public String[] getSuggestions() {
    return this.suggestions;
  }

  public int getPosition() {
    return this.position;
  }

  public boolean isRequired() {
    return this.required;
  }

  @NotNull
  public Class<?> getClazz() {
    return this.clazz;
  }

  @Override
  public String toString() {
    return "Argument: {Required: "
        + this.required
        + " Clazz: "
        + this.clazz
        + " Position: "
        + this.position
        + "}";
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public String getDescription() {
    return description;
  }
}
