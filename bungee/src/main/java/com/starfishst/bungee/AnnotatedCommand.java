package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.registry.ImplProvidersRegistry;
import com.starfishst.bungee.result.Result;
import com.starfishst.bungee.utils.Chat;
import com.starfishst.core.ICommandArray;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Lots;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/** The annotated command for bungee */
public class AnnotatedCommand extends net.md_5.bungee.api.plugin.Command
    implements ICommandArray<CommandContext> {

  /** The object that contains the method that invokes the command */
  @NotNull private final Object clazz;
  /** The method that is the command */
  @NotNull private final Method method;
  /** The arguments to get the parameters for the method */
  @NotNull private final List<ISimpleArgument<?>> arguments;
  /** The messages provider */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param clazz the object to invoke the command
   * @param method the method that needs to be invoked for the command
   * @param arguments the arguments to get the parameters for the command
   * @param command the annotation of the command to get the parameters
   * @param messagesProvider the messages provider
   */
  public AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull MessagesProvider messagesProvider) {
    super(
        command.aliases()[0],
        command.permission().isEmpty() ? null : command.permission(),
        Lots.remove(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result(messagesProvider.notAllowed(context));
      }
    }
    try {
      return (Result) this.method.invoke(this.clazz, getObjects(context));
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result("&cIllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      e.printStackTrace();
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result(e.getMessage());
      } else {
        return new Result("&cInvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new Result(e.getMessage());
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
  public List<ISimpleArgument<?>> getArguments() {
    return arguments;
  }

  @Override
  public @NotNull ProvidersRegistry<CommandContext> getRegistry() {
    return ImplProvidersRegistry.getInstance();
  }

  @Override
  public @NotNull IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }
}
