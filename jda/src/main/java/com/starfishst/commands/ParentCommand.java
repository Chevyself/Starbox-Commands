package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.result.IResult;
import com.starfishst.core.utils.Lots;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParentCommand extends AnnotatedCommand
        implements IParentCommand<AnnotatedCommand, CommandContext> {

    @NotNull
    private final List<AnnotatedCommand> commands = new ArrayList<>();

    public ParentCommand(
            @NotNull Object clazz,
            @NotNull Method method,
            Command cmd,
            @NotNull List<ISimpleArgument> arguments,
            @NotNull MessagesProvider messagesProvider) {
        super(clazz, method, cmd, arguments, messagesProvider);
    }

    @Override
    public @NotNull List<AnnotatedCommand> getCommands() {
        return commands;
    }

    @Nullable
    @Override
    public AnnotatedCommand getCommand(@NotNull String name) {
        for (AnnotatedCommand command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull IResult execute(@NotNull CommandContext context) {
        String[] strings = context.getStrings();
        if (strings.length >= 1) {
            AnnotatedCommand command = this.getCommand(strings[0]);
            if (command != null) {
                context.setStrings(Lots.arrayFrom(1, strings));
                return command.execute(context);
            } else {
                return super.execute(context);
            }
        } else {
            return super.execute(context);
        }
    }
}
