package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import com.starfishst.commands.objects.JoinedArgs;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class JoinedArgsProvider extends MultipleArgumentProvider<JoinedArgs> {

  public JoinedArgsProvider() {
    super(JoinedArgs.class);
  }

  @Override
  public JoinedArgs fromStrings(@NotNull String[] strings) throws ArgumentProviderException {
    return new JoinedArgs(strings);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    return new ArrayList<>();
  }

  @NotNull
  @Override
  public JoinedArgs fromString(@NotNull String string) throws ArgumentProviderException {
    return new JoinedArgs(string);
  }
}
