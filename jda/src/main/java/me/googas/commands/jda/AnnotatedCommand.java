package me.googas.commands.jda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
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
import me.googas.commands.time.Time;
import net.dv8tion.jda.api.Permission;

public class AnnotatedCommand extends EasyJdaCommand
    implements ReflectCommand<CommandContext, EasyJdaCommand> {

  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<EasyJdaCommand> children;

  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull Command command,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull List<EasyJdaCommand> children) {
    super(manager, command.excluded(), Time.of(command.cooldown()));
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.aliases = Arrays.asList(command.aliases());
    this.children = children;
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
  public Result execute(@NonNull CommandContext context) {
    Result result =
        this.manager.getPermissionChecker().checkPermission(context, this.getPermission());
    if (result != null) {
      return result;
    }
    result = checkCooldown(context.getSender(), context);
    if (result != null) {
      return result;
    }
    try {
      Object[] objects = getObjects(context);
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
