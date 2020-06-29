package com.starfishst.clean;

import com.starfishst.clean.commands.CleanComands;
import com.starfishst.clean.commands.FunCommands;
import com.starfishst.commands.CommandManager;
import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.messages.DefaultMessagesProvider;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.time.Time;
import java.util.HashMap;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

/** An example of a clean bot */
public class CleanBot {

  /**
   * The main method of the bot.
   *
   * <p>Arguments include:
   *
   * <p>'token' for the token of the bot
   *
   * @param args the desired arguments for the bot
   */
  public static void main(String[] args) {
    HashMap<String, String> argsMaps = Maps.fromStringArray("=", args);
    String token = argsMaps.getOrDefault("token", "");
    Scanner scanner = new Scanner(System.in);
    if (token.isEmpty()) {
      token = getTokenFromInput(scanner);
    }
    JDA jda = createConnection(token, scanner);
    CommandManager manager =
        new CommandManager(jda, "-", new ManagerOptions(), new DefaultMessagesProvider());
    manager.registerCommand(new CleanComands());
    manager.registerCommand(new FunCommands());
  }

  /**
   * Try to connect to jda
   *
   * @param token the initial token
   * @param scanner to get a new token in case that the initial token is wrong
   * @return the jda api connection
   */
  @NotNull
  public static JDA createConnection(@NotNull String token, @NotNull Scanner scanner) {
    JDA jda = null;
    while (jda == null) {
      try {
        jda = connect(token);
      } catch (LoginException e) {
        System.out.println("Discord authentication failed");
        token = getTokenFromInput(scanner);
      }
    }
    return jda;
  }

  /**
   * Get a token from the input of the console
   *
   * @param scanner to get the input from
   * @return the token if an input was made
   */
  @NotNull
  public static String getTokenFromInput(@NotNull Scanner scanner) {
    System.out.println("Insert the bot token");
    while (true) {
      if (scanner.hasNext()) {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("exit")) {
          System.exit(0);
        } else {
          return input;
        }
        break;
      }
    }
    return "";
  }

  /**
   * Connects to discord
   *
   * @param token the discord bot token
   * @return the jda api
   * @throws LoginException if the discord token is wrong and authentication failed
   */
  public static JDA connect(@NotNull String token) throws LoginException {
    JDA jda = JDABuilder.create(token, Lots.list(GatewayIntent.values())).build();
    long millis = 0;
    while (jda.getStatus() != JDA.Status.CONNECTED) {
      try {
        System.out.println("Waiting for discord connection...");
        Thread.sleep(1);
        millis++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(
        "Discord took " + Time.fromMillis(millis).toEffectiveString() + " to connect");
    return jda;
  }
}
