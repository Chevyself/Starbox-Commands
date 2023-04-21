package me.googas.testing;

import com.github.chevyself.starbox.jda.CommandManager;
import com.github.chevyself.starbox.jda.GenericListenerOptions;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.messages.MessagesProvider;
import com.github.chevyself.starbox.jda.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.jda.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.jda.providers.registry.JdaProvidersRegistry;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainTest {

  public static void main(String[] args) throws LoginException, InterruptedException {
    JDA jda =
        JDABuilder.create(args[0], Arrays.asList(GatewayIntent.values())).build().awaitReady();
    MessagesProvider messages = new JdaMessagesProvider();
    GenericListenerOptions options = new GenericListenerOptions();
    options.setPrefix("-");
    options.setDeleteSuccess(true);
    CommandManager manager =
        new CommandManager(new JdaProvidersRegistry(messages), messages, jda, options)
            .addGlobalMiddlewares(new CooldownMiddleware(), new PermissionMiddleware())
            .parseAndRegister(new TestCommands());
  }

  public static void deleteCommands(@NonNull JDA jda) {
    jda.retrieveCommands()
        .queue(commands -> commands.forEach(command -> jda.deleteCommandById(command.getIdLong())));
  }
}
