package me.googas.samples;

import java.util.List;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Optional;
import me.googas.commands.annotations.Required;
import me.googas.commands.arguments.Argument;
import me.googas.commands.context.StarboxCommandContext;

public class ArgumentsSample {

  public static void main(String[] args) throws NoSuchMethodException {
    List<Argument<?>> arguments =
        Argument.parseArguments(
            ArgumentsSample.class.getMethod(
                "AMethod",
                StarboxCommandContext.class,
                String.class,
                String.class,
                String[].class));
    for (Argument<?> argument : arguments) {
      System.out.println("argument = " + argument);
    }
    // Output:
    // argument = ExtraArgument{clazz=interface me.googas.commands.context.StarboxCommandContext}
    // argument = SingleArgument{name='No name provided', description='No description provided',
    // suggestions=[], clazz=class java.lang.String, required=true, position=0}
    // argument = SingleArgument{name='No name provided', description='No description provided',
    // suggestions=[], clazz=class java.lang.String, required=false, position=1}
    // argument = MultipleArgument{minSize=1, maxSize=-1} SingleArgument{name='No name provided',
    // description='No description provided', suggestions=[], clazz=class [Ljava.lang.String;,
    // required=true, position=2}
  }

  public void AMethod(
      StarboxCommandContext context,
      @Required String name,
      @Optional String description,
      @Required @Multiple String[] messages) {
    // Has 4 arguments
    // An ExtraArgument: the context
    // Two SingleArgument: The name and description
    // A MultipleArgument: the messages
    System.out.println(name + " has a description " + description);
  }

  public void AMethod(StarboxCommandContext context) {
    // The class of the argument is StarboxCommandContext
    System.out.println(context);
  }

  public void AMethod(@Required String name, @Optional(suggestions = "20") int age) {
    // A required argument is name
    // An optional argument is age
    System.out.println(name + " is " + age + " years old");
  }

  public void AMethod(@Multiple @Optional(suggestions = "Hello world") String message) {
    System.out.println(message);
  }
}
