package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.providers.BukkitArgumentProvider;
import com.starfishst.commands.providers.BukkitMultiArgumentProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.utils.Chat;
import com.starfishst.core.ICommand;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.ISimpleCommand;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import com.starfishst.core.providers.type.ISimpleArgumentProvider;
import com.starfishst.core.utils.Lots;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotatedCommand extends org.bukkit.command.Command
    implements ICommand<CommandContext> {

  @NotNull private final Object clazz;
  @NotNull private final Method method;
  @NotNull private final List<ISimpleArgument> arguments;

  AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument> arguments,
      @NotNull Command command) {
    super(
        command.aliases()[0], command.description(), "", Lots.removeAndList(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;

    final String permission = command.permission();
    if (!permission.isEmpty()) {
      this.setPermission(permission);
    }
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
    CommandSender sender = context.getSender();

    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result("&cYou are not allowed to use this command");
      }
    }
    // Parse method params
    final Object[] objects = new Object[this.arguments.size()];
    for (int i = 0; i < this.arguments.size(); i++) {
      ISimpleArgument argument = this.arguments.get(i);
      if (argument instanceof ExtraArgument) {
        IExtraArgumentProvider<?> provider =
                ICommandManager.getProvider(argument.getClazz(), IExtraArgumentProvider.class);
        if (provider != null) {
          try {
            objects[i] = provider.getObject(context);
          } catch (ArgumentProviderException e) {
            return new Result("&c{0}", e.getMessage());
          }
        } else {
          return new Result("&cProvider for {0} wasn't found", argument.getClazz());
        }
      } else if (argument instanceof Argument) {
        String string = ISimpleCommand.getArgument(((Argument) argument), context);
        if (string == null && ((Argument) argument).isRequired()) {
          return new Result(
              "&cMissing argument: &e{0}&c, position: &e{1}",
              ((Argument) argument).getName(), ((Argument) argument).getPosition());
        } else if (string == null && !((Argument) argument).isRequired()) {
          objects[i] = null;
        } else if (string != null) {
          ISimpleArgumentProvider<?> provider =
                  ICommandManager.getProvider(argument.getClazz(), ISimpleArgumentProvider.class);
          if (provider instanceof IMultipleArgumentProvider) {
            objects[i] =
                    ((IMultipleArgumentProvider<?>) provider)
                            .fromStrings(context.getStringsFrom(((Argument) argument).getPosition()));
          } else if (provider instanceof IArgumentProvider) {
            try {
              objects[i] = ((IArgumentProvider<?>) provider).fromString(string, context);
            } catch (ArgumentProviderException e) {
              return new Result("&c{0}", e.getMessage());
            }
          } else {
            return new Result("&cProvider for {0} wasn't found", argument.getClazz());
          }
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

  @NotNull
  @Override
  public Object getClazz() {
    return this.clazz;
  }

  @NotNull
  @Override
  public Method getMethod() {
    return this.method;
  }

  @NotNull
  @Override
  public List<ISimpleArgument> getArguments() {
    return this.arguments;
  }

  @Nullable
  private Argument getArgument(int position) {
    return (Argument)
        this.arguments.stream()
            .filter(
                argument ->
                    argument instanceof Argument && ((Argument) argument).getPosition() == position)
            .findFirst()
            .orElse(null);
  }

  @Override
  public boolean execute(
      @NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
    String message = this.execute(new CommandContext(commandSender, strings)).getMessage();
    if (message != null) {
      Chat.send(commandSender, message);
    }
    return true;
  }

  @Override
  public @NotNull List<String> tabComplete(
      @NotNull CommandSender sender, @NotNull String alias, @NotNull String[] strings)
      throws IllegalArgumentException {
    CommandContext context = new CommandContext(sender, strings);
    Argument argument = this.getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions(context).size() > 0) {
        return StringUtil.copyPartialMatches(
            strings[strings.length - 1], argument.getSuggestions(context), new ArrayList<>());
      } else {
        ISimpleArgumentProvider<?> provider =
                ICommandManager.getProvider(argument.getClazz(), ISimpleArgumentProvider.class);
        if (provider instanceof BukkitArgumentProvider) {
          return StringUtil.copyPartialMatches(
                  strings[strings.length - 1],
                  ((BukkitArgumentProvider<?>) provider).getSuggestions(context),
                  new ArrayList<>());
        } else if (provider instanceof BukkitMultiArgumentProvider) {
          return StringUtil.copyPartialMatches(
                  strings[strings.length - 1],
                  ((BukkitMultiArgumentProvider<?>) provider).getSuggestions(context),
                  new ArrayList<>());
        } else {
          return new ArrayList<>();
        }
      }
    } else {
      return new ArrayList<>();
    }
  }
}
