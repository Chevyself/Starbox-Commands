package com.starfishst.testbot;

import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The configuration for the test bot
 */
public class Configuration {

    @SerializedName("discord")
    @Nullable
    private final JDA jda;

    public Configuration(@NotNull JDA jda) {
        this.jda = jda;
    }

    @Nullable
    public JDA getJda() {
        return jda;
    }
}
