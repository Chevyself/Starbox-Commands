package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.ReflectCommand;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitMultiArgumentProvider;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.bukkit.result.Result;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.providers.type.StarboxContextualProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
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
   * @param manager the manager that parsed the command
   * @param name the name of the command
   * @param aliases other names that the command can be executed with
   * @param permission the permission required to run this command
   * @param description a short description of the command
   * @param usageMessage a helpful message to know how the command is properly executed
   * @param options the flags that apply in this command
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
   *     more in {@link Parent}
   */
  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull String name,
      @NonNull List<String> aliases,
      String permission,
      @NonNull String description,
      @NonNull String usageMessage,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull List<StarboxBukkitCommand> children) {
    super(manager, name, aliases, description, usageMessage, options, middlewares, async, cooldown);
    if (permission != null && !permission.isEmpty()) {
      this.setPermission(permission);
    }
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.children = children;
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
    FlagArgument.Parser parse = FlagArgument.parse(this.getOptions(), strings);
    strings = parse.getArgumentsArray();
    CommandContext context =
        new CommandContext(
            this,
            sender,
            parse.getArgumentsString(),
            strings,
            this.manager.getProvidersRegistry(),
            this.manager.getMessagesProvider(),
            parse.getFlags());
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
    if (this.getName().equalsIgnoreCase(alias)) {
      return true;
    }
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public BukkitResult execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof BukkitResult) {
        return (BukkitResult) object;
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
