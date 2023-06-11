package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.result.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.InternalExceptionResult;
import com.github.chevyself.starbox.result.StarboxResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractAnnotatedCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> implements ReflectCommand<C, T> {

  @NonNull @Getter protected final Object object;
  @NonNull @Getter protected final Method method;
  @NonNull @Getter protected final List<Argument<?>> arguments;

  protected AbstractAnnotatedCommand(
      @NonNull CommandManager<C, T> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method) {
    super(commandManager, annotation);
    this.object = object;
    this.method = method;
    this.arguments = Argument.parseArguments(method);
  }

  @Override
  public StarboxResult run(@NonNull C context) {
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof StarboxResult) {
        return (StarboxResult) object;
      } else {
        return null;
      }
    } catch (final IllegalAccessException | InvocationTargetException e) {
      return new InternalExceptionResult(e);
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new ArgumentExceptionResult(e);
    }
  }
}
