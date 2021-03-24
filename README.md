# Easy-Commands

This project aims to provide an easy creation of commands for Bukkit, Bungee, JDA and more to come. It includes some utilities to shorten the amount of lines in your project.

## Installing

1. Add the repository to your maven project

```xml
<repositories>
    <repository>
        <id>repsy</id>
        <url>https://repo.repsy.io/mvn/chevy/starfish</url>
    </repository>
</repositories>
```

2. Add the dependency
   
### Bukkit

```xml
<dependencies>
    <dependency>
        <groupId>com.starfishst.commands</groupId>
        <artifactId>bukkit</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Bungee

```xml
<dependencies>
    <dependency>
        <groupId>com.starfishst.commands</groupId>
        <artifactId>bungee</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### JDA

```xml
<dependencies>
    <dependency>
        <groupId>com.starfishst.commands</groupId>
        <artifactId>jda</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Core

This is used to create implementations of the `core` framework

```xml
<dependencies>
    <dependency>
        <groupId>com.starfishst.commands</groupId>
        <artifactId>core</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Usage

### Creating a command

1. Every single commands requires the `@Command` annotation and in each project it has different properties.
   
2. The annotation must be in the method that you want to use as a command. The method must return `Void` or `Result` else
when the command is being parsed the `CommandManager` will thrown an error. `Result` is also different in each module.
   
At the moment your method should look like this:

```java
public class Test {
    @Command(aliases = "command")
    public Result command() {
        return new Result();
    }
}
```

4. Simple as that! Now you can add all the logic you want to the command, parse and register it  in the `CommandManager`

5. You can also create parent commands which will make the execution as follows:

> (prefix)(parent) (command)

6. Just add the annotation `@Parent` to the method that should be the parent method.

### Arguments for commands

There is three types of arguments:

#### Extra

Extra arguments do not require any type of annotation and to register the provider it must extend `EasyExtraArgumentProvider`
or just implement the interface given by the module

In general extra arguments is the context of the command, the input, the sender, and such.

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender) {
        return new Result();
    }
}
```

#### Required and Optional

Both arguments require an input from the user, and the provider must extend `EasuArgumentProvider` or just implement the interface given by the module. The provider will never
return `null` as it may be needed as a required argument, and the `String` parameter is never null too.

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

It is just as `@Required` or `@Optional` but requires multiple strings, it requires any of the above annotation plus
`@Multiple` and the provider must extend `EasyMultipleArgumentProvider` or just implement the interface given by the module.

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender, @Required String name, @Optional long size, @Multiple @Optional JoinedStrings args) {
        return new Result();
    }
}            
```    

### Registering commands

1. Creating the Command Manager. Each `CommandManager` has its own options.

```java
CommandManager manager = new CommandManager(
        // Options of the command manager
        );
```

2. You can either create a command extending the class in the module that implements `EasyCommand` or using `CommandManager#parseCommand(Object)` which will get the commands using 
reflection as shown in [creating a command](#creating-a-command)

```java
manager.register(manager.parseCommand(cmd));
```

3. Registering each command using the method `EasyCommandManager#register(EasyCommand)` the manager will register the command.

```java
manager.registerCommand(new Test());
```

4. You are done! You've registered your commands.