package me.googas.commands.jda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.SingleArgument;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.exceptions.type.StarboxException;
import me.googas.commands.exceptions.type.StarboxRuntimeException;
import me.googas.commands.flags.Option;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.cooldown.CooldownManager;
import me.googas.commands.jda.middleware.JdaMiddleware;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * This is the direct extension of {@link JdaCommand} for reflection commands. This is returned from
 * {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends JdaCommand
    implements ReflectCommand<CommandContext, JdaCommand> {

  @NonNull @Getter private final List<JdaCommand> children = new ArrayList<>();
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<String> aliases;

  /**
   * Create a command.
   *
   * @param manager the manager that parsed the command
   * @param description a short description of the command
   * @param map a map that contains custom settings of the command
   * @param options the flags that apply to this command
   * @param middlewares the middlewares to run before and after this command is excuted
   * @param cooldown the manager that handles the cooldown in this command
   * @param aliases the names that the command can be executed with
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   */
  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull String description,
      @NonNull Map<String, String> map,
      @NonNull List<Option> options,
      @NonNull List<JdaMiddleware> middlewares,
      CooldownManager cooldown,
      @NonNull List<String> aliases,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments) {
    super(manager, description, map, options, middlewares, cooldown);
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.aliases = aliases;
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.manager.getProvidersRegistry();
  }

  @Override
  public @NonNull StarboxMessagesProvider<CommandContext> getMessagesProvider() {
    return this.manager.getMessagesProvider();
  }

  @Override
  public Result run(@NonNull CommandContext context) {
    try {
      Object[] objects = this.getObjects(context);
      Object object = this.method.invoke(this.object, objects);
      if (object instanceof Result) {
        Result result = (Result) object;
        if (result.getSuccess() == null && this.isExcluded()) {
          Message discordMessage = result.getDiscordMessage().orElse(null);
          result =
              Result.forType(result.getType())
                  .setMessage(() -> discordMessage)
                  .setDescription(result.getMessage().orElse(null))
                  .next(message -> {})
                  .build();
        }
        return result;
      }
      return null;
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      // return new Result(ResultType.UNKNOWN, "IllegalAccessException, e");
      return Result.forType(ResultType.UNKNOWN).setDescription("IllegalAccessException, e").build();
    } catch (final InvocationTargetException e) {
      final String message = e.getTargetException().getMessage();
      if (message != null && !message.isEmpty()) {
        if (!(e.getTargetException() instanceof StarboxException)
            | !(e.getTargetException() instanceof StarboxRuntimeException)) {
          e.printStackTrace();
        }
        return Result.forType(ResultType.ERROR).setDescription(message).build();
      } else {
        e.printStackTrace();
        return Result.forType(ResultType.UNKNOWN)
            .setDescription("InvocationTargetException, e")
            .build();
      }
    } catch (MissingArgumentException e) {
      return Result.forType(ResultType.USAGE).setDescription(e.getMessage()).build();
    } catch (ArgumentProviderException e) {
      return Result.forType(ResultType.ERROR).setDescription(e.getMessage()).build();
    }
  }

  private static OptionData toOptionData(@NonNull Argument<?> argument) {
    if (argument instanceof SingleArgument) {
      return new OptionData(
          AnnotatedCommand.toOptionType(argument.getClazz()),
          ((SingleArgument<?>) argument).getName(),
          ((SingleArgument<?>) argument).getDescription(),
          ((SingleArgument<?>) argument).isRequired());
    }
    return null;
  }

  @NonNull
  private static OptionType toOptionType(@NonNull Class<?> clazz) {
    if (clazz.isAssignableFrom(Number.class)) {
      return OptionType.INTEGER;
    } else if (clazz == boolean.class || clazz == Boolean.class) {
      return OptionType.BOOLEAN;
    } else if (clazz.isAssignableFrom(User.class) || clazz.isAssignableFrom(Member.class)) {
      return OptionType.USER;
    } else if (clazz.isAssignableFrom(MessageChannel.class)) {
      return OptionType.CHANNEL;
    } else if (clazz.isAssignableFrom(Role.class)) {
      return OptionType.ROLE;
    }
    return OptionType.STRING;
  }

  @Override
  public @NonNull SlashCommandData getCommandData() {
    SlashCommandData data = super.getCommandData();
    this.arguments.stream()
        .map(AnnotatedCommand::toOptionData)
        .filter(Objects::nonNull)
        .forEach(data::addOptions);
    return data;
  }
}
