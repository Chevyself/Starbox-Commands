package com.starfishst.commands.objects;

import com.starfishst.commands.utils.Strings;
import org.jetbrains.annotations.NotNull;

public class JoinedArgs {

  @NotNull private final String string;
  @NotNull private final String[] strings;

  public JoinedArgs(@NotNull String string) {
    this.string = string;
    this.strings = new String[0];
  }

  public JoinedArgs(@NotNull String[] strings) {
    this.strings = strings;
    this.string = Strings.fromArray(strings);
  }

  @NotNull
  public String getString() {
    return string;
  }

  @NotNull
  public String[] getStrings() {
    return strings;
  }
}
