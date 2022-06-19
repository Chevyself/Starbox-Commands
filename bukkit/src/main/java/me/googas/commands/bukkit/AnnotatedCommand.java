package me.googas.commands.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.SingleArgument;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.bukkit.providers.type.BukkitMultiArgumentProvider;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.StarboxContextualProvider;
import me.googas.commands.util.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/**
 * This is the direct extension of {@link StarboxBukkitCommand} for reflection commands. This is
 * returned from {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends StarboxBukkitCommand
    implements ReflectCommand<CommandContext, StarboxBukkitCommand> {

  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<StarboxBukkitCommand> children;

  /**
   * Create the command.
   *
   * @param command the annotation that will be used to get the name and aliases of the command
   *     {@link Command#aliases()} the description {@link Command#description()} whether to execute
   *     the command async {@link Command#async()} and the permission {@link Command#permission()}
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   * @param manager the manager that parsed the command
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   */
  public AnnotatedCommand(
      @NonNull Command command,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull CommandManager manager,
      @NonNull List<StarboxBukkitCommand> children) {
    super(
        command.aliases()[0],
        command.description(),
        "/"
            + Strings.buildUsageAliases(command.aliases())
            + " "
            + Argument.generateUsage(arguments),
        command.aliases().length > 1
            ? Arrays.asList(Arrays.copyOfRange(command.aliases(), 1, command.aliases().length))
            : new ArrayList<>(),
        command.async(),
        manager);
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.children = children;
    final String permission = command.permission();
    if (!permission.isEmpty()) {
      this.setPermission(permission);
    }
  }

  /**
   * Tab complete suggestions using reflection.
   *
   * @param sender The sender which will get the suggestions
   * @param strings the current strings in the command to be completed
   * @return the list of suggested strings
   */
  public @NonNull List<String> reflectTabComplete(
      @NonNull CommandSender sender, @NonNull String[] strings) {
    CommandContext context =
        new CommandContext(
            sender,
            strings,
            this.manager.getMessagesProvider(),
            this.manager.getProvidersRegistry());
    Optional<SingleArgument<?>> optionalArgument = this.getArgument(strings.length - 1);
    if (optionalArgument.isPresent()) {
      SingleArgument<?> argument = optionalArgument.get();
      if (argument.getSuggestions(context).size() > 0) {
        return StringUtil.copyPartialMatches(
            strings[strings.length - 1], argument.getSuggestions(context), new ArrayList<>());
      } else {
        List<StarboxContextualProvider<?, CommandContext>> providers =
            this.getRegistry().getProviders(argument.getClazz());
        for (StarboxContextualProvider<?, CommandContext> provider : providers) {
          if (provider instanceof BukkitArgumentProvider) {
            return StringUtil.copyPartialMatches(
                strings[strings.length - 1],
                ((BukkitArgumentProvider<?>) provider)
                    .getSuggestions(strings[strings.length - 1], context),
                new ArrayList<>());
          } else if (provider instanceof BukkitMultiArgumentProvider) {
            return StringUtil.copyPartialMatches(
                strings[strings.length - 1],
                ((BukkitMultiArgumentProvider<?>) provider).getSuggestions(context),
                new ArrayList<>());
          }
        }
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public @NonNull StarboxMessagesProvider<CommandContext> getMessagesProvider() {
    return this.manager.getMessagesProvider();
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.manager.getProvidersRegistry();
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    if (this.getName().equalsIgnoreCase(alias)) return true;
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return Result.of(this.manager.getMessagesProvider().notAllowed(context));
      }
    }
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof Result) {
        return (Result) object;
      } else {
        return null;
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return Result.of("&cIllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return Result.of("&c{0}");
      } else {
        e.printStackTrace();
        return Result.of("&cInvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return Result.of(e.getMessage());
    }
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      List<String> children =
          StringUtil.copyPartialMatches(
              strings[strings.length - 1], this.getChildrenNames(), new ArrayList<>());
      children.addAll(this.reflectTabComplete(sender, strings));
      return children;
    } else if (strings.length >= 2) {
      return this.getChildren(strings[0])
          .map(
              command ->
                  command.tabComplete(
                      sender, alias, Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(() -> this.reflectTabComplete(sender, strings));
    }
    return this.reflectTabComplete(sender, strings);
  }
}
