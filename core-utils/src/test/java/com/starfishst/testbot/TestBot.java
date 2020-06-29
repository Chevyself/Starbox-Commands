package com.starfishst.testbot;

import com.google.gson.JsonSyntaxException;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.files.CoreFiles;
import com.starfishst.utils.gson.GsonProvider;
import com.starfishst.utils.gson.adapters.jda.JdaAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A test bot
 */
public class TestBot {

    @NotNull
    private final Configuration configuration;

    public TestBot() throws IOException, BotInitException {
        GsonProvider.addAdapter(JDA.class, new JdaAdapter(null, Lots.list(GatewayIntent.values()), new AnnotatedEventManager(), new ArrayList<>()));
        GsonProvider.refresh();
        this.configuration = getConfiguration();
    }

    public static void main(String[] args) {
        try {
            new TestBot();
        } catch (IOException | BotInitException e) {
            System.out.println("Bot could not be initialized");
            e.printStackTrace();
        }
    }

    /**
     * Gets the configuration from the file
     * @return the configuration
     * @throws IOException if the config file is wrong
     * @throws BotInitException in case that the configuration cannot be initialized
     */
    @NotNull
    public Configuration getConfiguration() throws IOException, BotInitException {
        try {
            FileReader reader = new FileReader(CoreFiles.getFileOrResource(CoreFiles.currentDirectory(), "test.json"));
            Configuration configuration = GsonProvider.GSON.fromJson(reader, Configuration.class);
            reader.close();
            if (configuration == null) {
                throw new BotInitException("Bot could not be initialized because 'test.json' is empty!");
            }
            if (configuration.getJda() == null) {
                throw new BotInitException("Bot could not be initialized because a connection with discord could not be stabilised");
            }
            return configuration;
        } catch (JsonSyntaxException e) {
            throw new BotInitException("Bot could not be initialized because the json inside 'test.json' has a bad syntax!", e);
        }
    }

}
