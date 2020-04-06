package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.annotations.Optional;
import com.starfishst.commands.annotations.Required;
import com.starfishst.commands.listener.CommandListener;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.providers.CommandContextProvider;
import com.starfishst.commands.providers.GuildCommandContextProvider;
import com.starfishst.commands.providers.GuildProvider;
import com.starfishst.commands.providers.MemberProvider;
import com.starfishst.commands.providers.MemberSenderProvider;
import com.starfishst.commands.providers.MessageProvider;
import com.starfishst.commands.providers.RoleProvider;
import com.starfishst.commands.providers.TextChannelProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.type.ISimpleArgumentProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandManager implements ICommandManager<AnnotatedCommand> {

    static {
        ICommandManager.addProvider(new CommandContextProvider());
        ICommandManager.addProvider(new GuildCommandContextProvider());
        ICommandManager.addProvider(new GuildProvider());
        ICommandManager.addProvider(new MemberProvider());
        ICommandManager.addProvider(new MemberSenderProvider());
        ICommandManager.addProvider(new MessageProvider());
        ICommandManager.addProvider(new RoleProvider());
        ICommandManager.addProvider(new TextChannelProvider());
    }

    @NotNull
    private final List<AnnotatedCommand> commands = new ArrayList<>();
    @NotNull
    private final JDA jda;
    @NotNull
    private final MessagesProvider messagesProvider;
    @Nullable
    private ParentCommand parent;

    public CommandManager(
            @NotNull JDA jda,
            @NotNull String prefix,
            @NotNull ManagerOptions options,
            @NotNull MessagesProvider messagesProvider) {
        this.jda = jda;
        this.messagesProvider = messagesProvider;
        jda.addEventListener(new CommandListener(prefix, this, options, messagesProvider));
    }

    @Override
    public void registerCommand(@NotNull Object object) {
        final Class<?> clazz = object.getClass();
        if (clazz != null) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
                    this.parent = (ParentCommand) this.parseCommand(object, method, true);
                    this.commands.add(this.parent);
                }
            }
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
                    final AnnotatedCommand cmd = this.parseCommand(object, method, false);
                    if (this.parent != null) {
                        this.parent.addCommand(cmd);
                    } else {
                        this.commands.add(cmd);
                    }
                }
            }
            if (this.parent != null) this.parent = null;
        } else {
            throw new CommandRegistrationException(object + " class doesn't exist");
        }
    }

    @NotNull
    @Override
    public AnnotatedCommand parseCommand(
            @NotNull Object object, @NotNull Method method, boolean isParent) {
        if (method.getReturnType() == Result.class) {
            final Class<?>[] params = method.getParameterTypes();
            final Annotation[][] annots = method.getParameterAnnotations();
            final Command cmd = method.getAnnotation(Command.class);
            if (isParent) {
                return new ParentCommand(
                        object, method, cmd, parseArguments(params, annots), messagesProvider);
            } else {
                return new AnnotatedCommand(
                        object, method, cmd, parseArguments(params, annots), messagesProvider);
            }
        } else {
            throw new CommandRegistrationException(
                    "The method of the command must return Result @ " + method);
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

    private boolean isEmpty(@NotNull Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Required || annotation instanceof Optional) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public AnnotatedCommand getCommand(@NotNull String cmd) {
        for (final AnnotatedCommand command : this.commands) {
            for (final String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(cmd)) return command;
            }
        }
        return null;
    }

    public void addProvider(@NotNull ISimpleArgumentProvider<?> provider) {
        ICommandManager.addProvider(provider);
    }

    @NotNull
    public List<AnnotatedCommand> getCommands() {
        return commands;
    }

    @NotNull
    public MessagesProvider getMessagesProvider() {
        return messagesProvider;
    }

    @NotNull
    public JDA getJda() {
        return jda;
    }
}
