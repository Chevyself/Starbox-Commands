package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.type.BungeeArgumentProvider;
import com.starfishst.bungee.providers.type.BungeeMultiArgumentProvider;
import com.starfishst.bungee.result.Result;
import com.starfishst.core.ICommandArray;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ISimpleArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import me.googas.commons.Lots;
import me.googas.commons.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;

/** The annotated command for bungee */
public class AnnotatedCommand extends net.md_5.bungee.api.plugin.Command
    implements ICommandArray<CommandContext>, TabExecutor {

  /** The object that contains the method that invokes the command */
  @NotNull private final Object clazz;
  /** The method that is the command */
  @NotNull private final Method method;
  /** The arguments to get the parameters for the method */
  @NotNull private final List<ISimpleArgument<?>> arguments;
  /** The messages provider */
  @NotNull protected final MessagesProvider messagesProvider;
  /** The plugin where this command was registered */
  @NotNull protected final Plugin plugin;
  /** Whether the command should be executed asynchronously */
  private final boolean asynchronous;

  /** The providers registry for commands */
  @NotNull private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create an instance
   *
   * @param clazz the object to invoke the command
   * @param method the method that needs to be invoked for the command
   * @param arguments the arguments to get the parameters for the command
   * @param command the annotation of the command to get the parameters
   * @param messagesProvider the messages provider
   * @param plugin the plugin where this command was registered
   * @param asynchronous whether this command should run asynchronously
   * @param registry the registry for commands
   */
  public AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull MessagesProvider messagesProvider,
      @NotNull Plugin plugin,
      boolean asynchronous,
      ProvidersRegistry<CommandContext> registry) {
    super(
        command.aliases()[0],
        command.permission().isEmpty() ? null : command.permission(),
        Lots.remove(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.plugin = plugin;
    this.asynchronous = asynchronous;
    this.registry = registry;
  }

  /**
   * Run the command
   *
   * @param sender the sender of the command
   * @param strings the arguments of the command
   */
  private void run(CommandSender sender, String[] strings) {
    Result result = this.execute(new CommandContext(sender, strings, messagesProvider, registry));
    for (BaseComponent component : result.getComponents()) {
      sender.sendMessage(component);
    }
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
  public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
    CommandContext context = new CommandContext(commandSender, strings, messagesProvider, registry);
    Argument<?> argument = getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions(context).size() > 0) {
        return Strings.copyPartials(strings[strings.length - 1], argument.getSuggestions(context));
      } else {
        List<IContextualProvider<?, CommandContext>> providers =
            getRegistry().getProviders(argument.getClazz());
        for (IContextualProvider<?, CommandContext> provider : providers) {
          if (provider instanceof BungeeArgumentProvider) {
            return Strings.copyPartials(
                strings[strings.length - 1],
                ((BungeeArgumentProvider<?>) provider).getSuggestions(context));
          } else if (provider instanceof BungeeMultiArgumentProvider) {
            return Strings.copyPartials(
                strings[strings.length - 1],
                ((BungeeMultiArgumentProvider<?>) provider).getSuggestions(context));
          }
        }
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }

  /**
   * Get if this command is asynchronous
   *
   * @return true if it is
   */
  public boolean isAsynchronous() {
    return this.asynchronous;
  }

  @Override
  public void execute(CommandSender sender, String[] strings) {
    if (this.isAsynchronous()) {
      plugin.getProxy().getScheduler().runAsync(plugin, () -> run(sender, strings));
    } else {
      run(sender, strings);
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
    return this.registry;
  }

  @Override
  public @NotNull IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }
}
