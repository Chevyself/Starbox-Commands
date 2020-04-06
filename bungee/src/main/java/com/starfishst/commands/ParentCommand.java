package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.Result;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParentCommand extends AnnotatedCommand
        implements IParentCommand<AnnotatedCommand, CommandContext> {

    @NotNull
    private final List<AnnotatedCommand> commands = new ArrayList<>();

    public ParentCommand(
            Object object, Method method, List<ISimpleArgument> arguments, Command command) {
        super(object, method, arguments, command);
    }

    @Override
    public @NotNull List<AnnotatedCommand> getCommands() {
        return commands;
    }

    @Override
    public @NotNull Result execute(@NotNull CommandContext context) {
        String[] strings = context.getStrings();
        if (strings.length >= 1) {
            AnnotatedCommand command = this.getCommand(strings[0]);
            if (command != null) {
                return command.execute(
                        new CommandContext(
                                context.getSender(), Arrays.copyOfRange(strings, 1, strings.length)));
            } else {
                return super.execute(context);
            }
        } else {
            return super.execute(context);
        }
    }

    @Nullable
    @Override
    public AnnotatedCommand getCommand(@NotNull String name) {
        return this.commands.stream()
                .filter(
                        command -> {
                            if (command.getName().equalsIgnoreCase(name)) {
                                return true;
                            } else {
                                for (String alias : command.getAliases()) {
                                    if (alias.equalsIgnoreCase(name)) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        })
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addCommand(@NotNull AnnotatedCommand command) {
        this.commands.add(command);
    }
}
