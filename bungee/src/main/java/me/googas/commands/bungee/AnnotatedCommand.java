package me.googas.commands.bungee;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ICommandArray;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.bungee.annotations.Command;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.providers.type.BungeeArgumentProvider;
import me.googas.commands.bungee.providers.type.BungeeMultiArgumentProvider;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.objects.CommandSettings;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.IContextualProvider;
import me.googas.commands.utility.Series;
import me.googas.commands.utility.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

/** The annotated command for bungee */
public class AnnotatedCommand extends net.md_5.bungee.api.plugin.Command
    implements ICommandArray<CommandContext>, TabExecutor {

  @NonNull protected final MessagesProvider messagesProvider;
  /** The plugin where this command was registered */
  @NonNull @Getter protected final Plugin plugin;

  @NonNull private final Object clazz;
  @NonNull private final Method method;
  @NonNull private final List<ISimpleArgument<?>> arguments;
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  @NonNull private final CommandSettings settings;

  /**
   * Create an instance
   *
   * @param clazz the object to invoke the command
   * @param method the method that needs to be invoked for the command
   * @param arguments the arguments to get the parameters for the command
   * @param command the annotation of the command to get the parameters
   * @param messagesProvider the messages provider
   * @param plugin the plugin where this command was registered
   * @param registry the registry for commands
   * @param settings the settings of the command
   */
  public AnnotatedCommand(
      @NonNull Object clazz,
      @NonNull Method method,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull Command command,
      @NonNull MessagesProvider messagesProvider,
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull CommandSettings settings) {
    super(
        command.aliases()[0],
        command.permission().isEmpty() ? null : command.permission(),
        Series.remove(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.plugin = plugin;
    this.registry = registry;
    this.settings = settings;
  }

  /**
   * Run the command
   *
   * @param sender the sender of the command
   * @param strings the arguments of the command
   */
  private void run(CommandSender sender, String[] strings) {
    Result result = this.execute(new CommandContext(sender, strings, messagesProvider, registry));
    if (result != null) {
      for (BaseComponent component : result.getComponents()) {
        sender.sendMessage(component);
      }
    }
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result(messagesProvider.notAllowed(context));
      }
    }
    try {
      Object invoke = this.method.invoke(this.clazz, getObjects(context));
      if (invoke instanceof Result) {
        return (Result) invoke;
      }
      return null;
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

  @Override
  public void execute(CommandSender sender, String[] strings) {
    if (this.isAsynchronous()) {
      plugin.getProxy().getScheduler().runAsync(plugin, () -> run(sender, strings));
    } else {
      run(sender, strings);
    }
  }

  private boolean isAsynchronous() {
    return settings.containsFlag("-async", true) || settings.containsFlag("async", true);
  }

  @NonNull
  @Override
  public Object getClazz() {
    return clazz;
  }

  @NonNull
  @Override
  public Method getMethod() {
    return method;
  }

  @NonNull
  @Override
  public List<ISimpleArgument<?>> getArguments() {
    return arguments;
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.registry;
  }

  @Override
  public @NonNull IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public @NonNull CommandSettings getCommandArguments() {
    return this.settings;
  }
}
