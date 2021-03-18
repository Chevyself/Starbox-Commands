package me.googas.commands.bukkit;

import lombok.NonNull;
import me.googas.commands.ICommand;
import me.googas.commands.ICommandManager;
import me.googas.commands.ReflectCommand;
import me.googas.commands.bukkit.context.CommandContext;

import java.util.Collection;

public class NewCommandManager implements ICommandManager<CommandContext> {
    @Override
    public void register(@NonNull ICommand<CommandContext> command) {

    }

    @Override
    public @NonNull Collection<ReflectCommand<CommandContext>> parseCommands(@NonNull Object object) {
        return null;
    }

    @Override
    public @NonNull Collection<ICommand<CommandContext>> getCommands() {
        return null;
    }
}
