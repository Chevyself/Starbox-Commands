package com.starfishst.commands.result;

import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.messages.MessagesProvider;

import java.awt.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ResultType {
    ERROR("**Error**:", true),
    UNKNOWN("**Error**: → Unknown error", true),
    USAGE("**Error**: → Wrong usage", true),
    GENERIC("Success!", false),
    PERMISSION("**Error**: → No permission", true);

    @NotNull
    private final String title;
    private final boolean isError;

    ResultType(@NotNull String title, boolean isError) {
        this.title = title;
        this.isError = isError;
    }

    @NotNull
    public String getTitle(@Nullable MessagesProvider provider) {
        if (provider != null) return provider.getTitle(this);
        return title;
    }

    public boolean isError() {
        return isError;
    }

    @NotNull
    public Color getColor(@NotNull ManagerOptions options) {
        if (isError) {
            return options.getError();
        } else {
            return options.getSuccess();
        }
    }
}
