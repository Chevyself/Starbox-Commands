package me.googas.commands.jda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.exceptions.type.SimpleException;
import me.googas.commands.exceptions.type.SimpleRuntimeException;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.permissions.SimplePermission;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.utility.time.Time;
import net.dv8tion.jda.api.Permission;

/**
 * This is the direct extension of {@link EasyJdaCommand} for reflection commands this is returned
 * from {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends EasyJdaCommand
    implements ReflectCommand<CommandContext, EasyJdaCommand> {

  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<EasyJdaCommand> children = new ArrayList<>();

  /**
   * Create the command
   *
   * @param manager the manager that parsed the command
   * @param command the annotation that will be used to get the name and aliases of the command
   *     {@link Command#aliases()} whether to exclude the command {@link Command#excluded()} the
   *     cooldown {@link Command#cooldown()} and the permission {@link Command#permission()}
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link #getObjects(EasyCommandContext)}
   *     and invoke the {@link #getMethod()}
   */
  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull Command command,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments) {
    super(manager, command.excluded(), Time.of(command.cooldown()));
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.aliases = Arrays.asList(command.aliases());
    if (command.permission() != Permission.UNKNOWN || !command.node().isEmpty()) {
      this.setPermission(new SimplePermission(command.node(), command.permission()));
    }
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.manager.getProvidersRegistry();
  }

  @Override
  public @NonNull EasyMessagesProvider<CommandContext> getMessagesProvider() {
    return this.manager.getMessagesProvider();
  }

  @Override
  public Result run(@NonNull CommandContext context) {
    Result result =
        this.manager.getPermissionChecker().checkPermission(context, this.getPermission());
    if (result != null) {
      return result;
    }
    result = this.checkCooldown(context.getSender(), context);
    if (result != null) {
      return result;
    }
    try {
      Object[] objects = this.getObjects(context);
      Object object = this.method.invoke(this.object, objects);
      if (object instanceof Result) {
        result = (Result) object;
        if (this.getCooldown().toMillis() > 0) {
          this.getCooldownUsers()
              .add(
                  new CooldownUser(
                      this.getCooldown().toMillisRound(), context.getSender().getIdLong()));
        }
        if (result.getSuccess() == null && this.isExcluded()) {
          result =
              new Result(
                  result.getType(), result.getDiscordMessage(), result.getMessage(), message -> {});
        }
        return result;
      } else {
        return null;
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result(ResultType.UNKNOWN, "IllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getTargetException().getMessage();
      if (message != null && !message.isEmpty()) {
        if (!(e.getTargetException() instanceof SimpleException)
            | !(e.getTargetException() instanceof SimpleRuntimeException)) {
          e.printStackTrace();
        }
        return new Result(ResultType.ERROR, message);
      } else {
        e.printStackTrace();
        return new Result(ResultType.UNKNOWN, "InvocationTargetException, e");
      }
    } catch (MissingArgumentException e) {
      return new Result(ResultType.USAGE, e.getMessage());
    } catch (ArgumentProviderException e) {
      return new Result(ResultType.ERROR, e.getMessage());
    }
  }
}
