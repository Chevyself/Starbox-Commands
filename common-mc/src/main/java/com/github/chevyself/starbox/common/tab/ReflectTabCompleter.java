package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.commands.ArgumentedStarboxCommand;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

public abstract class ReflectTabCompleter<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>, O>
    extends GenericTabCompleter<C, T, O> {

  @NonNull
  public abstract C createContext(
      @NonNull CommandLineParser parser, @NonNull T command, @NonNull O sender);

  private @NonNull Optional<SingleArgument<?>> getArgument(@NonNull T command, int index) {
    if (command instanceof ArgumentedStarboxCommand) {
      return ((ArgumentedStarboxCommand<?, ?>) command).getArgument(index);
    } else {
      return Optional.empty();
    }
  }

  public @NonNull List<String> reflectTabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String[] strings) {
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    C context = this.createContext(parser, command, sender);
    Optional<SingleArgument<?>> optionalArgument = this.getArgument(command, strings.length - 1);
    if (optionalArgument.isPresent()) {
      SingleArgument<?> argument = optionalArgument.get();
      if (argument.getSuggestions(context).size() > 0) {
        return Strings.copyPartials(strings[strings.length - 1], argument.getSuggestions(context));
      } else {
        StarboxArgumentProvider<?, C> provider =
            command.getProvidersRegistry().getProvider(argument.getClazz());
        if (provider instanceof SuggestionsArgumentProvider) {
          return Strings.copyPartials(
              strings[strings.length - 1],
              ((SuggestionsArgumentProvider<?, C>) provider)
                  .getSuggestions(strings[strings.length - 1], context));
        }
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String name, @NonNull String[] strings) {
    String permission = this.getPermission(command);
    if (permission != null && !this.hasPermission(sender, permission)) {
      return new ArrayList<>();
    }
    if (strings.length == 1) {
      List<String> children =
          Strings.copyPartials(strings[strings.length - 1], command.getChildrenNames());
      children.addAll(this.reflectTabComplete(command, sender, strings));
      return children;
    } else if (strings.length >= 2) {
      return command
          .getChild(strings[0])
          .map(
              child ->
                  this.tabComplete(
                      child, sender, strings[0], Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(() -> this.reflectTabComplete(command, sender, strings));
    }
    return this.reflectTabComplete(command, sender, strings);
  }
}
