package com.starfishst.testbot;

import com.starfishst.core.exceptions.type.SimpleException;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when the bot could not be initialized
 */
public class BotInitException extends SimpleException {
    /**
     * Throw a simple exception
     *
     * @param message the message
     * @param cause   the cause of the exception
     */
    public BotInitException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Throw a simple exception
     *
     * @param message the message
     */
    public BotInitException(@NotNull String message) {
        super(message);
    }
}
