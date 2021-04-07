package me.googas.commands.bungee;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.SingleArgument;
import me.googas.commands.bungee.annotations.Command;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.providers.type.BungeeArgumentProvider;
import me.googas.commands.bungee.providers.type.BungeeMultiArgumentProvider;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.EasyContextualProvider;
import me.googas.utility.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * This is the direct extension of {@link BungeeCommand} for reflection commands this is returned
 * from {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends BungeeCommand
    implements ReflectCommand<CommandContext, BungeeCommand> {

  /** The plugin where this command was registered */
  @NonNull @Getter protected final Plugin plugin;

  @NonNull private final Object object;
  @NonNull private final Method method;
  @NonNull private final List<Argument<?>> arguments;

  /**
   * Create the command
   *
   * @param command the annotation that will be used to get the name and aliases of the command
   *     {@link Command#aliases()} whether to execute the command async {@link Command#async()} and
   *     the permission {@link Command#permission()}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   * @param manager the manager that parsed the command
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param arguments the list of arguments that are used to {@link #getObjects(EasyCommandContext)}
   *     and invoke the {@link #getMethod()}
   */
  public AnnotatedCommand(
      Command command,
      @NonNull List<BungeeCommand> children,
      @NonNull CommandManager manager,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull List<Argument<?>> arguments) {
    super(
        command.aliases()[0],
        command.permission().isEmpty() ? null : command.permission(),
        children,
        manager,
        command.async(),
        Arrays.copyOfRange(command.aliases(), 1, command.aliases().length));
    this.plugin = manager.getPlugin();
    this.object = object;
    this.method = method;
    this.arguments = arguments;
  }

  @NonNull
  public List<String> onReflectTabComplete(CommandSender sender, String[] strings) {
    CommandContext context =
        new CommandContext(
            sender, strings, manager.getMessagesProvider(), manager.getProvidersRegistry());
    SingleArgument<?> argument = getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions(context).size() > 0) {
        return Strings.copyPartials(strings[strings.length - 1], argument.getSuggestions(context));
      } else {
        List<EasyContextualProvider<?, CommandContext>> providers =
            getRegistry().getProviders(argument.getClazz());
        for (EasyContextualProvider<?, CommandContext> provider : providers) {
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

  @NonNull
  @Override
  public List<Argument<?>> getArguments() {
    return arguments;
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result(manager.getMessagesProvider().notAllowed(context));
      }
    }
    try {
      Object invoke = this.method.invoke(this.object, getObjects(context));
      if (invoke instanceof Result) {
        return (Result) invoke;
      }
      return null;
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result("&cIllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result(e.getMessage());
      } else {
        e.printStackTrace();
        return new Result("&cInvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new Result(e.getMessage());
    }
  }

  @NonNull
  @Override
  public Method getMethod() {
    return method;
  }

  @NonNull
  @Override
  public Object getObject() {
    return object;
  }

  @Override
  public @NonNull MessagesProvider getMessagesProvider() {
    return manager.getMessagesProvider();
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return manager.getProvidersRegistry();
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    if (strings.length == 1) {
      List<String> children =
          Strings.copyPartials(strings[strings.length - 1], this.getChildrenNames());
      children.addAll(onReflectTabComplete(sender, strings));
      return children;
    } else if (strings.length >= 2) {
      final BungeeCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.onTabComplete(sender, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return onReflectTabComplete(sender, strings);
      }
    } else {
      return onReflectTabComplete(sender, strings);
    }
  }
}
