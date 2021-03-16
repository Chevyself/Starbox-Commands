package me.googas.commands.objects;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

public class CommandSettings {

  /** The initial flags */
  @NonNull @Getter private final Set<String> flags;

  /** The properties */
  @NonNull @Getter @Delegate private final Properties properties;

  public CommandSettings(@NonNull Set<String> flags, @NonNull Properties properties) {
    this.flags = flags;
    this.properties = properties;
  }

  public CommandSettings() {
    this(new HashSet<>(), new Properties());
  }

  /**
   * Construct the program arguments from the initial array of strings
   *
   * @param args the initial array of strings
   * @return the constructed program arguments
   */
  @NonNull
  public static CommandSettings construct(String[] args) {
    if (args == null) return new CommandSettings();
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

  /**
   * Construct the program arguments from a string that will be split using ' '
   *
   * @param args the initial string
   * @return the constructed program arguments
   */
  @NonNull
  public static CommandSettings construct(String args) {
    if (args == null) return new CommandSettings();
    return CommandSettings.construct(args.split(" "));
  }

  /**
   * Check whether the program has the given flag
   *
   * @param flagToCheck the flag to check
   * @param ignoreCase whether to check the flag ignore case
   * @return true if the flag is in the set
   */
  public boolean containsFlag(String flagToCheck, boolean ignoreCase) {
    if (flagToCheck == null) return false;
    for (String flag : this.flags) {
      if (ignoreCase && flag.equalsIgnoreCase(flagToCheck)) return true;
      else if (flag.equals(flagToCheck)) return true;
    }
    return false;
  }

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
