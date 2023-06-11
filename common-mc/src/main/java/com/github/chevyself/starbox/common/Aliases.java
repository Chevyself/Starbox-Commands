package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.commands.StarboxCommand;
import java.util.Collection;
import lombok.NonNull;

public class Aliases {

  @NonNull
  public static String[] getAliases(Collection<String> aliases) {
    String[] aliasesArray = new String[aliases.size() - 1];
    for (int i = 1; i < aliases.size(); i++) {
      aliasesArray[i - 1] = aliases.toArray(new String[0])[i];
    }
    return aliasesArray;
  }

  @NonNull
  public static String[] getAliases(@NonNull StarboxCommand<?, ?> command) {
    return Aliases.getAliases(command.getAliases());
  }
}
