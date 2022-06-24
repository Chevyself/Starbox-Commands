package me.googas.commands.system;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.flags.FlagArgument;
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
  private boolean closed = false;

  /**
   * Create the listener.
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
    while (!closed) {
      if (this.scanner.hasNextLine()) {
        String line = this.scanner.nextLine().trim();
        String[] split = line.split(" ");
        String name = split[0];
        if (!name.startsWith(this.prefix)) continue;
        Optional<SystemCommand> optionalCommand =
            this.manager.getCommand(name.substring(this.prefix.length()));
        if (optionalCommand.isPresent()) {
          SystemCommand command = optionalCommand.get();
          FlagArgument.Parser parse =
              FlagArgument.parse(command.getOptions(), Arrays.copyOfRange(split, 1, split.length));
          Result result =
              command.execute(
                  new CommandContext(
                      command,
                      ConsoleCommandSender.INSTANCE,
                      parse.getArgumentsArray(),
                      parse.getArgumentsString(),
                      this.manager.getProvidersRegistry(),
                      this.manager.getMessagesProvider(),
                      parse.getFlags()));
          result
              .getMessage()
              .ifPresent(
                  message -> {
                    if (!message.isEmpty()) System.out.println(message);
                  });
        } else {
          System.out.println("Command " + name + " could not be found");
        }
      }
    }
  }

  /** Stops the listener. */
  public void finish() {
    closed = true;
  }
}
