package com.github.chevyself.starbox.arguments;

import com.github.chevyself.starbox.commands.ArgumentedStarboxCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

public class ArgumentsMap {

  @NonNull
  private final Map<String, Object> arguments;
  @NonNull
  private final List<Object> extra;

  public ArgumentsMap(@NonNull Map<String, Object> arguments, @NonNull List<Object> extra) {
    this.arguments = arguments;
    this.extra = extra;
  }

  @NonNull
  public Object get(@NonNull String name) {
    if (!this.arguments.containsKey(name)) {
      throw new IllegalArgumentException("No argument with name " + name + " found");
    }
    return this.arguments.get(name);
  }

}
