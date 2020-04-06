package com.starfishst.commands.messages;

import com.starfishst.commands.result.ResultType;
import org.jetbrains.annotations.NotNull;

/**
 * This is a default {@link MessagesProvider} to use if you don't want to create one of your own
 */
public class DefaultMessagesProvider implements MessagesProvider {
    @Override
    public @NotNull String commandNotFound() {
        return "Command not found";
    }

    @Override
    public @NotNull String footer() {
        return "";
    }

    @Override
    public @NotNull String getTitle(@NotNull ResultType type) {
        return type.getTitle(null);
    }

    @Override
    public @NotNull String response() {
        return "{0} -> {1}";
    }

    @Override
    public @NotNull String notAllowed() {
        return "You are not allowed to use this command";
    }

    @Override
    public @NotNull String guildOnly() {
        return "You may use this command in a guild";
    }

    @Override
    public @NotNull String missingArgument() {
        return "Missing argument: {0} -> {1}, position: {2}";
    }
}
