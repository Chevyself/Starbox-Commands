package me.googas.commands.objects;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import lombok.NonNull;
import me.googas.commons.ProgramArguments;

public class CommandSettings extends ProgramArguments {

  public CommandSettings(@NonNull Set<String> flags, @NonNull Properties properties) {
    super(flags, properties);
  }

  public CommandSettings() {}

  @NonNull
  public static CommandSettings constructCommand(String[] args) {
    if (args == null) {
      return new CommandSettings();
    } else {
      Set<String> flags = new HashSet<>();
      Properties properties = new Properties();
      for (String arg : args) {
        if (arg.contains("=")) {
          String[] split = arg.split("=");
          properties.put(split[0], split[1]);
        } else {
          flags.add(arg);
        }
      }
      return new CommandSettings(flags, properties);
    }
  }

  @NonNull
  public static CommandSettings constructCommand(String args) {
    return args == null ? new CommandSettings() : constructCommand(args.trim().split(" "));
  }
}
