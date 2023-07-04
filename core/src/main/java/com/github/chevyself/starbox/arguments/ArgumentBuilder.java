package com.github.chevyself.starbox.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

public class ArgumentBuilder<O> {

  @NonNull
  private final Class<O> clazz;
  @NonNull
  private String name;
  @NonNull
  private String description;
  @NonNull
  private List<String> suggestions;
  @NonNull
  private ArgumentBehaviour behaviour;
  private boolean required;

  public ArgumentBuilder(@NonNull Class<O> clazz) {
    this.clazz = clazz;
    this.name = clazz.getSimpleName();
    this.description = "No description provided";
    this.suggestions = new ArrayList<>();
    this.behaviour = ArgumentBehaviour.NORMAL;
    this.required = false;
  }

  @NonNull
  public ArgumentBuilder<O> setName(String name) {
    this.name = name;
    return this;
  }

  @NonNull
  public ArgumentBuilder<O> setDescription(@NonNull String description) {
    this.description = description;
    return this;
  }

  @NonNull
  public ArgumentBuilder<O> setSuggestions(@NonNull Collection<String> suggestions) {
    this.suggestions = new ArrayList<>(suggestions);
    return this;
  }

  @NonNull
  public ArgumentBuilder<O> setSuggestions(@NonNull String... suggestions) {
    this.suggestions = new ArrayList<>(Arrays.asList(suggestions));
    return this;
  }

  @NonNull
  public ArgumentBuilder<O> setBehaviour(
      @NonNull ArgumentBehaviour behaviour) {
    this.behaviour = behaviour;
    return this;
  }

  @NonNull
  public ArgumentBuilder<O> setRequired(boolean required) {
    this.required = required;
    return this;
  }

  @NonNull
  public SingleArgument<O> build(int index) {
    return new SingleArgument<>(this.name, this.description, this.suggestions, this.behaviour, this.clazz, this.required, index);
  }
}
