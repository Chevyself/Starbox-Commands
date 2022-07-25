package me.googas.commands.bungee;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.SingleArgument;
import me.googas.commands.bungee.annotations.Command;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.providers.type.BungeeArgumentProvider;
import me.googas.commands.bungee.providers.type.BungeeMultiArgumentProvider;
import me.googas.commands.bungee.result.BungeeResult;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.flags.Option;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.StarboxContextualProvider;
import me.googas.commands.util.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * This is the direct extension of {@link BungeeCommand} for reflection commands. This is returned
 * from {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends BungeeCommand
    implements ReflectCommand<CommandContext, BungeeCommand> {

  /** The plugin where this command was registered. */
  @NonNull @Getter protected final Plugin plugin;

  @NonNull private final Object object;
  @NonNull private final Method method;
  @NonNull private final List<Argument<?>> arguments;

  /**
   * Create the command.
   *
   * @param manager the manager that parsed the command
   * @param plugin the plugin that registers the command
   * @param name the name of the command
   * @param permission the permission required to run the command
   * @param options the flags that apply to this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param cooldown the manager that handles the cooldown in this command
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   * @param aliases other names that the command can be executed with
   */
  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull Plugin plugin,
      String name,
      String permission,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull List<BungeeCommand> children,
      String... aliases) {
    super(name, permission, children, manager, options, middlewares, async, cooldown, aliases);
    this.plugin = plugin;
    this.object = object;
    this.method = method;
    this.arguments = arguments;
  }

  /**
   * Tab complete suggestions using reflection.
   *
   * @param sender The sender which will get the suggestions
   * @param strings the current strings in the command to be completed
   * @return the list of suggested strings
   */
  @NonNull
  public List<String> onReflectTabComplete(CommandSender sender, String[] strings) {
    FlagArgument.Parser parse = FlagArgument.parse(this.getOptions(), strings);
    strings = parse.getArgumentsArray();
    CommandContext context =
        new CommandContext(
            this,
            sender,
            parse.getArgumentsArray(),
            parse.getArgumentsString(),
            this.manager.getProvidersRegistry(),
            this.manager.getMessagesProvider(),
            parse.getFlags());
    Optional<SingleArgument<?>> optionalArgument = this.getArgument(strings.length - 1);
    if (optionalArgument.isPresent()) {
      SingleArgument<?> argument = optionalArgument.get();
      if (argument.getSuggestions(context).size() > 0) {
        return Strings.copyPartials(strings[strings.length - 1], argument.getSuggestions(context));
      } else {
        List<StarboxContextualProvider<?, CommandContext>> providers =
            this.getRegistry().getProviders(argument.getClazz());
        for (StarboxContextualProvider<?, CommandContext> provider : providers) {
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
    return this.arguments;
  }

  @Override
  public BungeeResult execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return Result.of(this.manager.getMessagesProvider().notAllowed(context));
      }
    }
    try {
      Object invoke = this.method.invoke(this.object, this.getObjects(context));
      if (invoke instanceof BungeeResult) {
        return (BungeeResult) invoke;
      }
      return null;
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return Result.of("&cIllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return Result.of(e.getMessage());
      } else {
        e.printStackTrace();
        return Result.of("&cInvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return Result.of(e.getMessage());
    }
  }

  @NonNull
  @Override
  public Method getMethod() {
    return this.method;
  }

  @NonNull
  @Override
  public Object getObject() {
    return this.object;
  }

  @Override
  public @NonNull MessagesProvider getMessagesProvider() {
    return this.manager.getMessagesProvider();
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.manager.getProvidersRegistry();
  }

  @Override
  @NonNull
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    if (strings.length == 1) {
      List<String> children =
          Strings.copyPartials(strings[strings.length - 1], this.getChildrenNames());
      children.addAll(this.onReflectTabComplete(sender, strings));
      return children;
    } else if (strings.length >= 2) {
      return this.getChildren(strings[0])
          .map(
              command ->
                  command.onTabComplete(sender, Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(ArrayList::new);
    }
    return this.onReflectTabComplete(sender, strings);
  }
}
