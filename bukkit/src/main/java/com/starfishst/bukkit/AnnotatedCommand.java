package com.starfishst.bukkit;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.registry.ImplProvidersRegistry;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.bukkit.providers.type.BukkitMultiArgumentProvider;
import com.starfishst.bukkit.result.Result;
import com.starfishst.bukkit.utils.Chat;
import com.starfishst.core.ICommand;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import com.starfishst.core.utils.Lots;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The annotated command for bukkit */
public class AnnotatedCommand extends org.bukkit.command.Command
    implements ICommand<CommandContext> {

  /** The object that owns the method to invoke */
  @NotNull private final Object clazz;
  /** The method to invoke. The method to invoke is the one annotated */
  @NotNull private final Method method;
  /** The arguments of the command */
  @NotNull private final List<ISimpleArgument<?>> arguments;
  /** The provider for messages */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param clazz the object that owns the method to invoke
   * @param method the method to invoke
   * @param arguments the arguments of the command
   * @param command the annotation that has all the command information
   * @param messagesProvider the provider for messages
   */
  AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull MessagesProvider messagesProvider) {
    super(
        command.aliases()[0], command.description(), "", Lots.removeAndList(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    final String permission = command.permission();
    if (!permission.isEmpty()) {
      this.setPermission(permission);
    }
  }

  /**
   * Get the argument of certain position
   *
   * @param position the position to get the argument from
   * @return the argument if exists else null
   */
  @Nullable
  private Argument<?> getArgument(int position) {
    return (Argument<?>)
        this.arguments.stream()
            .filter(
                argument ->
                    argument instanceof Argument
                        && ((Argument<?>) argument).getPosition() == position)
            .findFirst()
            .orElse(null);
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
        return new Result("&c{0}");
      } else {
        return new Result("&cInvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new Result(e.getMessage());
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
  public List<ISimpleArgument<?>> getArguments() {
    return this.arguments;
  }

  @Override
  public @NotNull ProvidersRegistry<CommandContext> getRegistry() {
    return ImplProvidersRegistry.getInstance();
  }

  @Override
  public @NotNull IMessagesProvider<?, CommandContext> getMessagesProvider() {
    return messagesProvider;
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
    Argument<?> argument = this.getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions(context).size() > 0) {
        return StringUtil.copyPartialMatches(
            strings[strings.length - 1], argument.getSuggestions(context), new ArrayList<>());
      } else {
        List<IContextualProvider<?, CommandContext>> providers =
            getRegistry().getProviders(argument.getClazz());
        for (IContextualProvider<?, CommandContext> provider : providers) {
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
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }
}
