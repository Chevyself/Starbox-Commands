package me.googas.commands.bukkit;

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
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.bukkit.providers.type.BukkitMultiArgumentProvider;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.EasyContextualProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class AnnotatedCommand extends BukkitCommand
    implements ReflectCommand<CommandContext, BukkitCommand> {

  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<BukkitCommand> children;

  public AnnotatedCommand(
      @NonNull Command command,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull CommandManager manager,
      @NonNull List<BukkitCommand> children) {
    super(
        command.aliases()[0],
        command.description(),
        "",
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

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return manager.getRegistry();
  }

  @Override
  public @NonNull EasyMessagesProvider<CommandContext> getMessagesProvider() {
    return manager.getMessagesProvider();
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
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof Result) {
        return (Result) object;
      } else {
        return null;
      }
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

  @Override
  public boolean hasAlias(@NonNull String alias) {
    if (this.getName().equalsIgnoreCase(alias)) return true;
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }

  public @NonNull List<String> reflectTabComplete(
      @NonNull CommandSender sender, @NonNull String[] strings) {
    CommandContext context =
        new CommandContext(sender, strings, manager.getMessagesProvider(), manager.getRegistry());
    SingleArgument<?> argument = this.getArgument(strings.length - 1);
    if (argument != null) {
      if (argument.getSuggestions(context).size() > 0) {
        return StringUtil.copyPartialMatches(
            strings[strings.length - 1], argument.getSuggestions(context), new ArrayList<>());
      } else {
        List<EasyContextualProvider<?, CommandContext>> providers =
            getRegistry().getProviders(argument.getClazz());
        for (EasyContextualProvider<?, CommandContext> provider : providers) {
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
  public @NonNull List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.getChildrenNames(), new ArrayList<>());
    } else if (strings.length >= 2) {
      final BukkitCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return reflectTabComplete(sender, strings);
      }
    } else {
      return reflectTabComplete(sender, strings);
    }
  }
}
