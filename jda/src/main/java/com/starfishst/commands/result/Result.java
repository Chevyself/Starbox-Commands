package com.starfishst.commands.result;

import com.starfishst.core.result.IResult;
import com.starfishst.core.utils.Strings;

import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is your general type of result
 */
public class Result implements IResult {

    @NotNull
    private final ResultType type;
    @Nullable
    private final Message discordMessage;
    @Nullable
    private final String message;
    @Nullable
    private final Consumer<Message> success;

    public Result(
            @NotNull ResultType type,
            @Nullable Message discordMessage,
            @Nullable Consumer<Message> success,
            @Nullable String message,
            Object... strings) {
        this.discordMessage = discordMessage;
        this.type = type;
        this.message = Strings.buildMessageOrNull(message, strings);
        this.success = success;
    }

    public Result(@NotNull ResultType type, @Nullable String message, Object... strings) {
        this(type, null, null, message, strings);
    }

    public Result(@Nullable String message, Object... strings) {
        this(ResultType.GENERIC, message, strings);
    }

    public Result() {
        this(ResultType.GENERIC, null);
    }

    @NotNull
    public ResultType getType() {
        return this.type;
    }

    /**
     * This success is a callback for when the process is completed
     *
     * @return the callback
     */
    @Nullable
    public Consumer<Message> getSuccess() {
        return this.success;
    }

    @Override
    public @Nullable String getMessage() {
        return message;
    }

    @Nullable
    public Message getDiscordMessage() {
        return discordMessage;
    }
}
