package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.annotations.Optional;
import com.starfishst.commands.annotations.Parent;
import com.starfishst.commands.annotations.Required;
import com.starfishst.commands.exceptions.CommandRegistrationException;
import com.starfishst.commands.objects.Argument;
import com.starfishst.commands.objects.CommandContext;
import com.starfishst.commands.objects.Result;
import com.starfishst.commands.providers.ArgumentProvider;
import com.starfishst.commands.providers.DoubleProvider;
import com.starfishst.commands.providers.IntegerProvider;
import com.starfishst.commands.providers.LongProvider;
import com.starfishst.commands.providers.PlayerProvider;
import com.starfishst.commands.providers.StringProvider;
import com.starfishst.commands.topic.AnnotatedCommandHelpTopicFactory;
import com.starfishst.commands.topic.PluginHelpTopic;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandManager {

  @NotNull
  private static final List<ArgumentProvider<?>> argumentProviderList = new ArrayList<>();
  @NotNull
  private static final Server server = Bukkit.getServer();
  @NotNull
  private static final HelpMap helpMap = server.getHelpMap();
  @NotNull
  private static final CommandMap commandMap;

  static {
    try {
      final Class<? extends Server> serverClass = server.getClass();
      final Field commandMapField = serverClass.getDeclaredField("commandMap");
      commandMapField.setAccessible(true);
      commandMap = (CommandMap) commandMapField.get(server);

      helpMap.registerHelpTopicFactory(
          AnnotatedCommand.class, new AnnotatedCommandHelpTopicFactory());
      registerArgumentProvider(new DoubleProvider());
      registerArgumentProvider(new IntegerProvider());
      registerArgumentProvider(new IntegerProvider());
      registerArgumentProvider(new LongProvider());
      registerArgumentProvider(new StringProvider());
      registerArgumentProvider(new PlayerProvider());
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new CommandRegistrationException("Command map could not be accessed");
    }
  }

  private final List<AnnotatedCommand> commands = new ArrayList<>();
  @NotNull
  private final Plugin plugin;
  @Nullable
  private ParentCommand parent;

  public CommandManager(@NotNull final Plugin plugin) {
    this.plugin = plugin;
  }

  public static void registerArgumentProvider(@NotNull ArgumentProvider<?> provider) {
    argumentProviderList.add(provider);
  }

  @NotNull
  private static AnnotatedCommand parseMethod(
      @NotNull final Object obj, @NotNull final Method method, final boolean parent) {
    if (method.getReturnType() == Result.class) {
      final Class<?>[] parameters = method.getParameterTypes();
      final Annotation[][] annotations = method.getParameterAnnotations();
      final Command command = method.getAnnotation(Command.class);
      if (parent) {
        return new ParentCommand(
            obj, method, command, CommandManager.getArguments(parameters, annotations));
      } else {
        return new AnnotatedCommand(
            obj, method, command, CommandManager.getArguments(parameters, annotations));
      }
    } else {
      throw new CommandRegistrationException(
          "The method of the command must return " + Result.class + " @ " + method);
    }
  }

  @NotNull
  private static Argument[] getArguments(
      @NotNull final Class<?>[] parameters, @NotNull final Annotation[][] annotations) {
    final Argument[] arguments = new Argument[parameters.length];
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      if (CommandContext.class.isAssignableFrom(parameters[i])) {
        arguments[i] =
            new Argument(
                parameters[i],
                CommandManager.isRequired(annotations[i]),
                -1,
                CommandManager.getDefault(annotations[i]));
      } else if (!CommandManager.isEmpty(annotations[i])) {
        arguments[i] =
            new Argument(
                parameters[i],
                CommandManager.isRequired(annotations[i]),
                position,
                CommandManager.getDefault(annotations[i]),
                getName(annotations[i]),
                getDescription(annotations[i]));
        position++;
      } else {
        throw new CommandRegistrationException(
            parameters[i]
                + " does not have either of the two command annotations: @Required | @Optional");
      }
    }
    return arguments;
  }

  private static String getName(@NotNull Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation.annotationType() == Required.class) {
        return ((Required) annotation).name();
      }
      if (annotation.annotationType() == Optional.class) {
        return ((Optional) annotation).name();
      }
    }
    return "No name provided";
  }

  private static String[] getDefault(final Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation.annotationType() == Required.class) {
        return ((Required) annotation).suggestions();
      } else if (annotation.annotationType() == Optional.class) {
        return ((Optional) annotation).suggestions();
      }
    }
    return new String[0];
  }

  private static boolean isEmpty(@NotNull final Object[] objects) {
    final List<Object> objects1 = Arrays.asList(objects);
    return objects1.isEmpty();
  }

  private static boolean isRequired(@NotNull final Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation.annotationType() == Required.class) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  private static String getDescription(@NotNull final Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation.annotationType() == Required.class) {
        return ((Required) annotation).description();
      }
      if (annotation.annotationType() == Optional.class) {
        return ((Optional) annotation).description();
      }
    }
    return "No description provided";
  }

  @Nullable
  static ArgumentProvider<?> getProvider(Class<?> clazz) {
    for (ArgumentProvider<?> provider : argumentProviderList) {
      if (provider.getClazz() == clazz) {
        return provider;
      }
    }
    return null;
  }

  @NotNull
  static List<ArgumentProvider<?>> getArgumentProviderList() {
    return argumentProviderList;
  }

  public void registerCommands(@NotNull final Object obj) {
    final Class<?> clazz = obj.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        this.parent = (ParentCommand) CommandManager.parseMethod(obj, method, true);
        commandMap.register(this.plugin.getName(), this.parent);
        this.commands.add(this.parent);
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
        final AnnotatedCommand cmd = CommandManager.parseMethod(obj, method, false);
        if (this.parent != null) {
          this.parent.add(cmd);
        } else {
          commandMap.register(this.plugin.getName(), cmd);
          this.commands.add(cmd);
        }
      }
    }
    parent = null;
  }

  public void registerPlugin() {
    helpMap.addTopic(new PluginHelpTopic(this, plugin));
  }

  public List<AnnotatedCommand> getCommands() {
    return commands;
  }
}
