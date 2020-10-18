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
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.providers.type.IContextualProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import me.googas.commons.Lots;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

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
  @NotNull protected final MessagesProvider messagesProvider;
  /** The plugin where this command was registered */
  @NotNull protected final Plugin plugin;
  /** Whether the command should be executed asynchronously */
  private final boolean asynchronous;
  /** The registry for the command */
  @NotNull private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create an instance
   *
   * @param clazz the object that owns the method to invoke
   * @param method the method to invoke
   * @param arguments the arguments of the command
   * @param command the annotation that has all the command information
   * @param messagesProvider the provider for messages
   * @param plugin the plugin where this command was registered
   * @param asynchronous whether the command should execute asynchronously
   * @param registry the registry for the command context
   */
  AnnotatedCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull MessagesProvider messagesProvider,
      @NotNull Plugin plugin,
      boolean asynchronous,
      @NotNull ProvidersRegistry<CommandContext> registry) {
    super(
        command.aliases()[0], command.description(), "", Lots.removeAndList(command.aliases(), 0));
    this.clazz = clazz;
    this.method = method;
    this.arguments = arguments;
    this.messagesProvider = messagesProvider;
    this.plugin = plugin;
    this.asynchronous = asynchronous;
    this.registry = registry;
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
  private void run(@NotNull CommandSender commandSender, @NotNull String[] strings) {
    Result result =
        this.execute(new CommandContext(commandSender, strings, messagesProvider, registry));
    for (BaseComponent component : result.getComponents()) {
      commandSender.sendMessage(component.toLegacyText());
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
  public @NotNull Result execute(@NotNull CommandContext context) {
    CommandSender sender = context.getSender();
    final String permission = this.getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!sender.hasPermission(permission)) {
        return new Result(messagesProvider.notAllowed(context));
      }
    }
    try {
      return (Result) this.method.invoke(this.clazz, this.getObjects(context));
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
  public @NotNull IMessagesProvider<CommandContext> getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public @NotNull ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
    if (asynchronous) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> run(commandSender, strings));
    } else {
      run(commandSender, strings);
    }
    return true;
  }

  @Override
  public @NotNull List<String> tabComplete(
      @NotNull CommandSender sender, @NotNull String alias, @NotNull String[] strings)
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
