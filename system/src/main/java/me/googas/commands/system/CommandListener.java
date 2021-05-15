package me.googas.commands.system;

import java.util.Arrays;
import java.util.Scanner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.system.context.sender.ConsoleCommandSender;

/**
 * This object is a {@link Thread} with a {@link Scanner} which waits for {@link System#in}. This
 * checks that the line starts with the prefix if it does then it will check that the name matches a
 * {@link SystemCommand} in {@link CommandManager#getCommand(String)} if it does it will execute the
 * command and the {@link Result} if it is not empty will be printed in the console
 */
public class CommandListener extends Thread {

  @NonNull private final Scanner scanner = new Scanner(System.in);
  @NonNull private final CommandManager manager;
  @NonNull @Getter private final String prefix;

  /**
   * Create the listener
   *
   * @param manager the command manager to get the commands
   * @param prefix the prefix to differentiate messages from commands
   */
  public CommandListener(@NonNull CommandManager manager, @NonNull String prefix) {
    this.manager = manager;
    this.prefix = prefix;
  }

  @Override
  public void run() {
    while (true) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        String[] split = line.split(" ");
        String name = split[0];
        if (!name.startsWith(prefix)) continue;
        SystemCommand command = manager.getCommand(name.substring(1));
        if (command != null) {
          Result result =
              command.execute(
                  new CommandContext(
                      ConsoleCommandSender.INSTANCE,
                      Arrays.copyOfRange(split, 1, split.length),
                      manager.getProvidersRegistry(),
                      manager.getMessagesProvider()));
          String message = result.getMessage();
          if (!message.isEmpty()) System.out.println(message);
        } else {
          System.out.println("Command " + name + " could not be found");
        }
      }
    }
  }
}
