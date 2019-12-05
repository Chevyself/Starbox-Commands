package com.starfishst.core;

import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.providers.DoubleProvider;
import com.starfishst.core.providers.IntegerProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.providers.type.ISimpleArgumentProvider;
import com.starfishst.core.utils.Lots;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class is used to register commands */
public interface ICommandManager<C extends ICommand> {

  List<ISimpleArgumentProvider> providers =
      Lots.list(
          new DoubleProvider(),
          new IntegerProvider(),
          new JoinedStringsProvider(),
          new LongProvider(),
          new StringProvider());

  /**
   * Register a command in {@link ICommandManager}
   *
   * @param object the class instance of the command
   */
  void registerCommand(@NotNull Object object);

  /**
   * Parse the arguments of a command
   *
   * @param parameters the parameters of the command method
   * @param annotations the annotations of the parameters of the command method
   * @return the list of parsed {@link ISimpleArgument} empty if theres none
   */
  @NotNull
  List<ISimpleArgument> parseArguments(
      @NotNull final Class<?>[] parameters, @NotNull final Annotation[][] annotations);

  /**
   * Get a new instance of {@link ICommand}
   *
   * @param object the class instance of the command
   * @param method the method to run the command
   * @param isParent is the command a parent
   * @return the new instance of {@link ICommand}
   */
  @NotNull
  C parseCommand(@NotNull Object object, @NotNull Method method, boolean isParent);

  /**
   * Add a {@link IArgumentProvider} to the manager
   *
   * @param provider the provider to add
   */
  static void addProvider(@NotNull ISimpleArgumentProvider provider) {
    ICommandManager.providers.add(provider);
  }

  /**
   * Get a provider using its class
   *
   * @param clazz the class to get the provider from
   * @return the provider if found
   */
  @Nullable
  static <I extends ISimpleArgumentProvider> I getProvider(
      @NotNull Class clazz, @NotNull Class<I> providerClass) {
    return providerClass.cast(
        ICommandManager.providers.stream()
            .filter(
                provider ->
                    provider.getClazz() == clazz
                        && providerClass.isAssignableFrom(provider.getClass()))
            .findFirst()
            .orElse(null));
  }

  /**
   * Get a argument using the command annotations
   *
   * @param parameter the parameter of the command
   * @param annotations the annotations of the command
   * @param position the position of the parameter
   * @return the argument made with the annotations
   */
  @NotNull
  Argument parseArgument(Class parameter, Annotation[] annotations, int position);
}
