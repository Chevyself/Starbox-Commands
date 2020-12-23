package com.starfishst.bukkit;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.bukkit.providers.type.BukkitMultiArgumentProvider;
import com.starfishst.bukkit.result.Result;
import com.starfishst.core.ICommand;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ISimpleArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.objects.CommandSettings;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commons.Lots;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

/** The annotated command for bukkit */
public class AnnotatedCommand extends org.bukkit.command.Command
    implements ICommand<CommandContext> {

  /** The provider for messages */
  @NonNull protected final MessagesProvider messagesProvider;
  /** The plugin where this command was registered */
  @NonNull @Getter protected final Plugin plugin;

  @NonNull private final Object clazz;
  @NonNull private final Method method;
  @NonNull private final List<ISimpleArgument<?>> arguments;
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  @NonNull private final CommandSettings commandSettings;

  /**
   * Create an instance
   *
   * @param clazz the object that owns the method to invoke
   * @param method the method to invoke
   * @param arguments the arguments of the command
   * @param command the annotation that has all the command information
   * @param messagesProvider the provider for messages
   * @param plugin the plugin where this command was registered
   * @param registry the registry for the command context
   * @param commandSettings
   */
  AnnotatedCommand(
      @NonNull Object clazz,
      @NonNull Method method,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull Command command,
      @NonNull MessagesProvider messagesProvider,
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull CommandSettings commandSettings) {
    super(
        command.aliases()[0], command.description(), "", Lots.removeAndList(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.plugin = plugin;
    this.registry = registry;
    this.commandSettings = commandSettings;
    final String permission = command.permission();
    if (!permission.isEmpty()) {
      this.setPermission(permission);
    }
  }

  /**
   * Run the command
   *
   * @param commandSender the sender of the command
   * @param strings the strings of the command
   */
  private void run(@NonNull CommandSender commandSender, @NonNull String[] strings) {
    Result result =
        this.execute(new CommandContext(commandSender, strings, messagesProvider, registry));
    if (result != null) {
      for (BaseComponent component : result.getComponents()) {
        commandSender.sendMessage(component.toLegacyText());
      }
    }
  }

  @NonNull
  @Override
  public Object getClazz() {
    return this.clazz;
  }

  @NonNull
  @Override
  public Method getMethod() {
    return this.method;
  }

  @NonNull
  @Override
  public List<ISimpleArgument<?>> getArguments() {
    return this.arguments;
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
      Object object = this.method.invoke(this.clazz, this.getObjects(context));
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
  public @NonNull IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }

  private boolean isAsynchronous() {
    return commandSettings.containsFlag("-async", true)
        || commandSettings.containsFlag("async", true);
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @Override
  public @NonNull CommandSettings getCommandArguments() {
    return this.commandSettings;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender commandSender, @NonNull String s, @NonNull String[] strings) {
    if (isAsynchronous()) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> run(commandSender, strings));
    } else {
      run(commandSender, strings);
    }
    return true;
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String alias, @NonNull String[] strings)
      throws IllegalArgumentException {
    CommandContext context = new CommandContext(sender, strings, messagesProvider, registry);
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
}
