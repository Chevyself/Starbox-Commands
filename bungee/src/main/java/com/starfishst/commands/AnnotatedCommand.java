package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.utils.Chat;
import com.starfishst.core.ICommandArray;
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
import java.util.List;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AnnotatedCommand extends net.md_5.bungee.api.plugin.Command
        implements ICommandArray<CommandContext> {

  @NotNull
  private final Object clazz;
  @NotNull
  private final Method method;
  @NotNull
  private final List<ISimpleArgument> arguments;

  public AnnotatedCommand(
          @NotNull Object clazz,
          @NotNull Method method,
          @NotNull List<ISimpleArgument> arguments,
          @NotNull Command command) {
    super(
            command.aliases()[0],
            command.permission().isEmpty() ? null : command.permission(),
            Lots.remove(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
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

  @Override
  public void execute(CommandSender sender, String[] strings) {
    String message = this.execute(new CommandContext(sender, strings)).getMessage();
    if (message != null) {
      Chat.send(sender, message);
    }
  }

  @NotNull
  @Override
  public Object getClazz() {
    return clazz;
  }

  @NotNull
  @Override
  public Method getMethod() {
    return method;
  }

  @NotNull
  @Override
  public List<ISimpleArgument> getArguments() {
    return arguments;
  }
}
