package com.starfishst.commands.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandSenderArgumentProvider implements IExtraArgumentProvider<CommandSender> {

    @Override
    public @NotNull Class<CommandSender> getClazz() {
        return CommandSender.class;
    }

    @NotNull
    @Override
    public CommandSender getObject(@NotNull ICommandContext<?> context) {
        return (CommandSender) context.getSender();
    }
}
