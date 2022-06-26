package me.googas.testing;

import java.util.Arrays;
import javax.security.auth.login.LoginException;
import lombok.NonNull;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.GenericListenerOptions;
import me.googas.commands.jda.messages.JdaMessagesProvider;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.middleware.CooldownMiddleware;
import me.googas.commands.jda.middleware.PermissionMiddleware;
import me.googas.commands.jda.providers.registry.JdaProvidersRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainTest {

  public static void main(String[] args) throws LoginException, InterruptedException {
    JDA jda =
        JDABuilder.create(args[0], Arrays.asList(GatewayIntent.values())).build().awaitReady();
    MessagesProvider messages = new JdaMessagesProvider();
    CommandManager manager =
        new CommandManager(
            new JdaProvidersRegistry(messages),
            messages,
            jda,
            new GenericListenerOptions().setPrefix("-")).addMiddleware(new CooldownMiddleware(),
            new PermissionMiddleware());
  }

  public static void deleteCommands(@NonNull JDA jda) {
    jda.retrieveCommands()
        .queue(commands -> commands.forEach(command -> jda.deleteCommandById(command.getIdLong())));
  }
}
