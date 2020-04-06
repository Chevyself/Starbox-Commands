package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class CommandContextProvider implements IExtraArgumentProvider<CommandContext> {

    @Override
    public @NotNull Class<CommandContext> getClazz() {
        return CommandContext.class;
    }

    @NotNull
    @Override
    public CommandContext getObject(@NotNull ICommandContext<?> context) {
        return (CommandContext) context;
    }
}
