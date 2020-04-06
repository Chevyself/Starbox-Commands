package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.ICommand;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.ISimpleCommand;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.context.IMappable;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import com.starfishst.core.providers.type.ISimpleArgumentProvider;
import com.starfishst.core.result.IResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class AnnotatedCommand implements ICommand<CommandContext>, IMappable {

  @NotNull
  private final Object clazz;
  @NotNull
  private final Method method;
  @NotNull
  private final String name, description;
  @NotNull
  private final List<String> aliases;
  @NotNull
  private final Permission permission;
  @NotNull
  private final List<ISimpleArgument> arguments;
  @NotNull
  private final MessagesProvider messagesProvider;

  public AnnotatedCommand(
          @NotNull Object clazz,
          @NotNull Method method,
          Command cmd,
          @NotNull List<ISimpleArgument> arguments,
          @NotNull MessagesProvider messagesProvider) {
    this.clazz = clazz;
    this.method = method;
    this.name = cmd.aliases()[0];
    this.description = cmd.description();
    this.aliases = Arrays.asList(cmd.aliases());
    this.permission = cmd.permission();
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull IResult execute(@NotNull CommandContext context) {
    if (this.permission != Permission.UNKNOWN && context.getMessage().getMember() != null) {
      if (!context.getMessage().getMember().hasPermission(this.permission)) {
        return new Result(ResultType.PERMISSION, messagesProvider.notAllowed());
      }
    } else if (this.permission != Permission.UNKNOWN && context.getMessage().getMember() == null) {
      return new Result(ResultType.ERROR, messagesProvider.guildOnly());
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
            return new Result(ResultType.ERROR, e.getMessage());
          }
        } else {
          return new Result(ResultType.ERROR, "Provider for {0} wasn't found", argument.getClazz());
        }
      } else if (argument instanceof Argument) {
        String string = ISimpleCommand.getArgument(((Argument) argument), context);
        if (string == null && ((Argument) argument).isRequired()) {
          return new Result(
                  ResultType.USAGE,
                  messagesProvider.missingArgument(),
                  ((Argument) argument).getName(),
                  ((Argument) argument).getDescription(),
                  ((Argument) argument).getPosition());
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
              return new Result(ResultType.ERROR, e.getMessage());
            }
          } else {
            return new Result(
                    ResultType.ERROR, "Provider for {0} wasn't found", argument.getClazz());
          }
        }
      }
    }
    try {
      return (Result) this.method.invoke(this.clazz, objects);
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result(ResultType.UNKNOWN, "IllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result(ResultType.ERROR, message);
      } else {
        e.printStackTrace();
        return new Result(ResultType.UNKNOWN, "InvocationTargetException, e");
      }
    }
  }

  @NotNull
  @Override
  public List<String> getAliases() {
    return aliases;
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

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getDescription() {
    return description;
  }
}
