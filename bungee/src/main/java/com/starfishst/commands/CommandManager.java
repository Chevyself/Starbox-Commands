package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.annotations.Optional;
import com.starfishst.commands.annotations.Required;
import com.starfishst.commands.result.Result;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.CommandRegistrationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandManager implements ICommandManager<AnnotatedCommand> {

    @NotNull
    private final Plugin plugin;
    @NotNull
    private final PluginManager manager;
    @Nullable
    private ParentCommand parent;

    public CommandManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getProxy().getPluginManager();
    }

    @Override
    public void registerCommand(@NotNull Object object) {
        final Class<?> clazz = object.getClass();
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
                this.parent = (ParentCommand) this.parseCommand(object, method, true);
                this.manager.registerCommand(this.plugin, this.parent);
            }
        }
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
                final AnnotatedCommand cmd = this.parseCommand(object, method, false);
                if (this.parent != null) {
                    this.parent.addCommand(cmd);
                } else {
                    this.manager.registerCommand(this.plugin, cmd);
                }
            }
        }
        this.parent = null;
    }

    /**
     * Checks if a parameter has {@link com.starfishst.commands.annotations.Required} or {@link
     * com.starfishst.commands.annotations.Optional}
     *
     * @param annotations the parameter annotations to check
     * @return true if doesn't have required or optional
     */
    private boolean isEmpty(@NotNull Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Required || annotation instanceof Optional) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull List<ISimpleArgument> parseArguments(
            @NotNull Class<?>[] parameters, @NotNull Annotation[][] annotations) {
        List<ISimpleArgument> arguments = new ArrayList<>();
        int position = 0;
        for (int i = 0; i < parameters.length; i++) {
            Annotation[] paramAnnotations = annotations[i];
            if (this.isEmpty(paramAnnotations)) {
                arguments.add(i, new ExtraArgument(parameters[i]));
            } else {
                arguments.add(i, this.parseArgument(parameters[i], annotations[i], position));
                position++;
            }
        }
        return arguments;
    }

    @NotNull
    @Override
    public AnnotatedCommand parseCommand(
            @NotNull Object object, @NotNull Method method, boolean isParent) {
        if (method.getReturnType() == Result.class) {
            // Parameter annotations
            Annotation[][] annotations = method.getParameterAnnotations();
            Class<?>[] parameters = method.getParameterTypes();
            Command command = method.getAnnotation(Command.class);

            if (isParent) {
                return new ParentCommand(
                        object, method, this.parseArguments(parameters, annotations), command);
            } else {
                return new AnnotatedCommand(
                        object, method, this.parseArguments(parameters, annotations), command);
            }
        } else {
            throw new CommandRegistrationException("{0} must return {1}", method, Result.class);
        }
    }

    @Override
    public @NotNull Argument parseArgument(
            Class<?> parameter, Annotation[] annotations, int position) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Required) {
                Required required = (Required) annotation;
                return new Argument(
                        required.name(),
                        required.description(),
                        Arrays.asList(required.suggestions()),
                        parameter,
                        true,
                        position);
            } else if (annotation instanceof Optional) {
                Optional optional = (Optional) annotation;
                return new Argument(
                        optional.name(),
                        optional.description(),
                        Arrays.asList(optional.suggestions()),
                        parameter,
                        false,
                        position);
            }
        }
        throw new CommandRegistrationException(
                "Argument could not be parsed as the method does not contain {0} or {1}",
                Required.class, Optional.class);
    }
}
