# Easy-Commands

This project aims to provide an easy creation of commands for Bukkit, Bungee, JDA and more to come. It includes some utilities to shorten the amount of lines in your project.

## Installing

1. Add the repository to your maven project

```xml
<repositories>
    <repository>
        <id>bintray-xchevy-Starfish</id>
        <name>xchevy-Starfish</name>
        <url>https://dl.bintray.com/xchevy/Starfish</url>
    </repository>
</repositories>
```

2. Add the dependency. Replace `x` to the module of the plugin that you need and `version` to the latest version of the module. The name is self-explanatory.
Use `bukkit`, `bungee` or `jda`.
   
```xml
<dependencies>
    <dependency>
        <groupId>com.starfishst.commands.x</groupId>
        <artifactId>x</artifactId>
        <version>version</version>
    </dependency>
</dependencies>
```

## Usage

### Creating a command

1. Every single commands requires the `@Command` annotation and in each project it has different properties. To see more about
the properties of the annotation in each module go to the wiki.
   
2. The annotation must be in the method that you want to use as a command. The method must return `Void` or `Result` else
when the command is being parsed the `CommandManager` will thrown an error. `Result` is also different in each module. You can see more about this object in the wiki.
   
At the moment your method should look like this:

```java
public class Test {
    @Command(name = "command")
    public Result command() {
        return new Result();
    }
}
```

4. Simple as that! Now you can add all the logic you want to the command and register it in the `CommandManager`

5. You can also create parent commands which will make the execution as follows:

> (prefix)(parent) (command)

6. Just add the annotation `@Parent` to the method that should be the parent method.

### Arguments for commands

There is three types of arguments:

#### Extra

Extra arguments do not require any type of annotation and to register the provider it must extend `IExtraArgumentProvider`
or just implement the interface given by the module. See more in the wiki.

In general extra arguments is the context of the command, the sender, and such.

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender) {
        return new Result();
    }
}
```

#### Required and Optional

Both arguments require an input from the user, and the provider must extend `IArgumentProvider` or just implement the interface given by the module. See more in the wiki. The provider will never
return `null` as it may be needed as a required argument, and the string parameter is never null too.

It is easy to understand each annotation as a `NonNull` and `Nullable`. This may be used as arguments for a different
output in the logic.

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender, @Required String name, @Optional long size) {
        return new Result();
    }
}            
```            

#### Multiple

It is just as required or optional but requires multiple strings, it requires any of the above annotation plus
`@Multiple` and the provider must extend `IMultipleArgumentProvider` or just implement the interface given by the module. See more in the wiki.

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender, @Required String name, @Optional long size, @Multiple @Optional JoinedStrings args) {
        return new Result();
    }
}            
```    

### Registering commands

1. Creating the Command Manager. Each `CommandManager` has its own options. That's why you should check the wiki.

```java
CommandManager manager = new CommandManager(
        // Options of the command manager
        );
```

2. Registering each command using the method `ICommandManager#registerCommand(Object` the manager will parse the command and register in the respective command map.

```java
manager.registerCommand(new Test());
```

3. You are done! You've registered your commands.