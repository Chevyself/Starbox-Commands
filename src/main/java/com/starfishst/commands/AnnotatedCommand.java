package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.exceptions.ArgumentProviderException;
import com.starfishst.commands.objects.Argument;
import com.starfishst.commands.objects.CommandContext;
import com.starfishst.commands.objects.Result;
import com.starfishst.commands.providers.ArgumentProvider;
import com.starfishst.commands.utils.Chat;
import com.starfishst.commands.utils.Strings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotatedCommand extends org.bukkit.command.Command {

  @NotNull
  private final Object clazz;
  @NotNull
  private final Method method;
  @NotNull
  private final Argument[] arguments;

  AnnotatedCommand(
      @NotNull final Object clazz,
      @NotNull final Method method,
      final Command command,
      @NotNull final Argument[] arguments) {
    super(
        command.aliases()[0],
        command.description(),
        command.usage(),
        Strings.removeFirstAlias(command.aliases()));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;

    final String permission = command.permission();
    if (!permission.isEmpty()) {
      this.setPermission(permission);
    }
  }

  @Nullable
  private static String getArgument(
      @NotNull final Argument argument, @NotNull final String[] strings) {
    if (strings.length - 1 < argument.getPosition() | argument.getPosition() == -1) {
      if (!argument.isRequired() & argument.getSuggestions().length > 0) {
        return argument.getSuggestions()[0];
      } else {
        return null;
      }
    } else {
      return strings[argument.getPosition()];
    }
  }

  Result execute(@NotNull final CommandSender sender, @NotNull final String[] strings) {
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result("&cYou are not allowed to use this command");
      }
    }
    final Object[] objects = new Object[this.arguments.length];
    for (int i = 0; i < this.arguments.length; i++) {
      final Argument argument = this.arguments[i];
      final String string = AnnotatedCommand.getArgument(argument, strings);
      final Class<?> clazz = argument.getClazz();
      if (CommandContext.class == clazz) {
        objects[i] = new CommandContext(sender, this.getName(), strings);
      } else if (string == null) {
        if (argument.isRequired()) {
          return new Result(
              "&cMissing argument: &e{0}&c, position: &e{1}",
              argument.getName(), argument.getPosition());
        } else {
          objects[i] = null;
        }
      } else {
        boolean cached = false;
        for (ArgumentProvider<?> argumentProvider : CommandManager.getArgumentProviderList()) {
          final Class<?> clazz1 = argumentProvider.getClazz();
          if (clazz1.isAssignableFrom(clazz)) {
            try {
              objects[i] = argumentProvider.fromString(string);
              cached = true;
            } catch (ArgumentProviderException e) {
              return new Result(e.getMessage());
            }
            break;
          }
        }
        if (!cached) {
          objects[i] = string;
        }
      }
    }
    try {
      return (Result) this.method.invoke(this.clazz, objects);
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result("&cIllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      e.printStackTrace();

      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result("&c{0}", message);
      } else {
        return new Result("&cInvocationTargetException, e");
      }
    }
  }

  @Override
  public boolean execute(
      @NotNull final CommandSender sender,
      @NotNull final String commandLabel,
      @NotNull final String[] strings) {
    final Result result = this.execute(sender, strings);
    if (result.getMessage() != null) {
      Chat.send(sender, result.getMessage(), result.getStrings());
    }
    return true;
  }

  @NotNull
  @Override
  public List<String> tabComplete(
      @NotNull final CommandSender sender, @NotNull final String alias, final String[] strings)
      throws IllegalArgumentException {
    final Argument argument = this.getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions().length > 0) {
        return StringUtil.copyPartialMatches(
            strings[strings.length - 1],
            Arrays.asList(argument.getSuggestions()),
            new ArrayList<>());
      } else {
        ArgumentProvider<?> provider = CommandManager.getProvider(argument.getClazz());
        if (provider != null) {
          return StringUtil.copyPartialMatches(
              strings[strings.length - 1], provider.suggestions(sender), new ArrayList<>());
        } else {
          return new ArrayList<>();
        }
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Nullable
  private Argument getArgument(final int position) {
    for (final Argument argument : this.arguments) {
      if (argument.getPosition() == position) {
        return argument;
      }
    }
    return null;
  }

  public Argument[] getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "AnnotatedCommand{" + "description='" + description + '\'' + '}';
  }
}
