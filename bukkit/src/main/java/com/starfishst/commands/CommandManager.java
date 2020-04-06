package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.annotations.Optional;
import com.starfishst.commands.annotations.Required;
import com.starfishst.commands.providers.CommandContextProvider;
import com.starfishst.commands.providers.CommandSenderArgumentProvider;
import com.starfishst.commands.providers.PlayerProvider;
import com.starfishst.commands.providers.PlayerSenderProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.topic.AnnotatedCommandHelpTopicFactory;
import com.starfishst.commands.topic.PluginHelpTopic;
import com.starfishst.commands.utils.BukkitUtils;
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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandManager implements ICommandManager<AnnotatedCommand> {

    @NotNull
    private static final List<AnnotatedCommand> commands = new ArrayList<>();
    @NotNull
    private static final HelpMap helpMap = Bukkit.getServer().getHelpMap();
    @NotNull
    private static final CommandMap commandMap;

    static {
        try {
            commandMap = BukkitUtils.getCommandMap();

            CommandManager.helpMap.registerHelpTopicFactory(
                    AnnotatedCommand.class, new AnnotatedCommandHelpTopicFactory());

            ICommandManager.addProvider(new CommandContextProvider());
            ICommandManager.addProvider(new PlayerProvider());
            ICommandManager.addProvider(new PlayerSenderProvider());
            ICommandManager.addProvider(new CommandSenderArgumentProvider());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Command Map could not be accessed");
        }
    }

    @NotNull
    private final Plugin plugin;
    @NotNull
    private final CommandManagerOptions options;
    @Nullable
    private ParentCommand parent = null;

    public CommandManager(@NotNull Plugin plugin, @NotNull CommandManagerOptions options) {
        this.plugin = plugin;
        this.options = options;
    }

    public CommandManager(@NotNull Plugin plugin) {
        this(plugin, new CommandManagerOptions(false));
    }

    /**
     * Get the commands registered in the manager
     *
     * @return the commands registered in the manager
     */
    @NotNull
    public static List<AnnotatedCommand> getCommands() {
        return CommandManager.commands;
    }

    @Override
    public void registerCommand(@NotNull Object object) {
        final Class<?> clazz = object.getClass();
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
                this.parent = (ParentCommand) this.parseCommand(object, method, true);
                CommandManager.commandMap.register(this.plugin.getName(), this.parent);
                CommandManager.commands.add(this.parent);
            }
        }
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
                final AnnotatedCommand cmd = this.parseCommand(object, method, false);
                if (this.parent != null) {
                    this.parent.addCommand(cmd);
                } else {
                    CommandManager.commandMap.register(this.plugin.getName(), cmd);
                    CommandManager.commands.add(cmd);
                }
            }
        }
        this.parent = null;
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
    public @NotNull AnnotatedCommand parseCommand(
            @NotNull Object object, @NotNull Method method, boolean isParent) {
        if (method.getReturnType() == Result.class) {
            // Parameter annotations
            Annotation[][] annotations = method.getParameterAnnotations();
            Class<?>[] parameters = method.getParameterTypes();
            Command command = method.getAnnotation(Command.class);

            if (isParent) {
                return new ParentCommand(
                        object, method, this.parseArguments(parameters, annotations), command, options);
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

  /**
   * Registers the plugin used for the {@link CommandManager} into the {@link HelpMap} (/help) do
   * this after you've registered all your commands so they can be shown
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this, this.plugin));
  }

  @NotNull
  public CommandManagerOptions getOptions() {
    return this.options;
  }
}
