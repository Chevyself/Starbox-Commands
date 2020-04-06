package com.starfishst.commands;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

public class ManagerOptions {

    private boolean deleteCommands = false;
    private boolean embedMessages = true;
    private boolean deleteErrors = true;
    private boolean addThumbnail = true;
    private int deleteTime = 15;
    @NotNull
    private TimeUnit deleteUnit = TimeUnit.SECONDS;
    @NotNull
    private Color success = new Color(0x02e9ff);
    @NotNull
    private Color error = new Color(0xff0202);

    public boolean isDeleteCommands() {
        return this.deleteCommands;
    }

    public void setDeleteCommands(final boolean deleteCommands) {
        this.deleteCommands = deleteCommands;
    }

    public int getDeleteTime() {
        return this.deleteTime;
    }

    public void setDeleteTime(final int deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean isEmbedMessages() {
        return this.embedMessages;
    }

    public void setEmbedMessages(final boolean embedMessages) {
        this.embedMessages = embedMessages;
    }

    public boolean isDeleteErrors() {
        return this.deleteErrors;
    }

    public void setDeleteErrors(final boolean deleteErrors) {
        this.deleteErrors = deleteErrors;
    }

    @NotNull
    public Color getError() {
        return this.error;
    }

    public void setError(@NotNull final Color error) {
        this.error = error;
    }

    @NotNull
    public Color getSuccess() {
        return this.success;
    }

    public void setSuccess(@NotNull final Color success) {
        this.success = success;
    }

    @NotNull
    public TimeUnit getDeleteUnit() {
        return this.deleteUnit;
    }

    public void setDeleteUnit(@NotNull final TimeUnit deleteUnit) {
        this.deleteUnit = deleteUnit;
    }

    public boolean isAddThumbnail() {
        return addThumbnail;
    }

    public void setAddThumbnail(boolean addThumbnail) {
        this.addThumbnail = addThumbnail;
    }
}
