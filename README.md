Starbox-Commands [![Jitpack](https://jitpack.io/v/me.googas/starbox-commands.svg)](https://jitpack.io/#me.googas/starbox-commands)

===
This project aims to provide an easy creation of commands for Bukkit, Bungee, JDA and more to come. It includes some utilities to shorten the amount of lines in your project.

Installing
--------
1. Add the repository to your maven project

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

2. Add the dependency
   
### Bukkit

```xml
<dependency>
  <groupId>me.googas.starbox-commands</groupId>
  <artifactId>bukkit</artifactId>
  <version>tag</version>
</dependency>
```

### Bungee

```xml
<dependency>
  <groupId>me.googas.starbox-commands</groupId>
  <artifactId>bungee</artifactId>
  <version>Tag</version>
</dependency>
```

### JDA

```xml
<dependency>
  <groupId>me.googas.starbox-commands</groupId>
  <artifactId>jda</artifactId>
  <version>Tag</version>
</dependency>
```

### Core

This is used to create implementations of the `core` framework

```xml
<dependency>
  <groupId>me.googas.starbox-commands</groupId>
  <artifactId>core</artifactId>
  <version>Tag</version>
</dependency>
```

Usage
--------

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

There are three types of arguments:

#### Extra

Extra arguments do not require any type of annotation and to register the provider it must extend `StarboxExtraArgumentProvider`
or just implement the interface given by the module:

* Bukkit: [BukkitExtraArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bukkit/src/main/java/me/googas/commands/bukkit/providers/type/BukkitExtraArgumentProvider.java)
* Bungee: [BungeeExtraArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bungee/src/main/java/me/googas/commands/bungee/providers/type/BungeeExtraArgumentProvider.java)
* JDA: [JdaExtraArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/jda/src/main/java/me/googas/commands/jda/providers/type/JdaExtraArgumentProvider.java)

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

Both arguments require an input from the user, and the provider must extend `Starbox` or just implement the interface given by the module: The provider will never
return `null` as it may be needed as a required argument, and the `String` parameter is never null too.

* Bukkit: [BukkitArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bukkit/src/main/java/me/googas/commands/bukkit/providers/type/BukkitArgumentProvider.java)
* Bungee: [BungeeArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bungee/src/main/java/me/googas/commands/bungee/providers/type/BungeeArgumentProvider.java)
* JDA: [JdaArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/jda/src/main/java/me/googas/commands/jda/providers/type/JdaArgumentProvider.java)

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
`@Multiple` and the provider must extend `StarboxMultipleArgumentProvider` or just implement the interface given by the module.

* Bukkit: [BukkitMultiArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bukkit/src/main/java/me/googas/commands/bukkit/providers/type/BukkitMultiArgumentProvider.java)
* Bungee: [BungeeMultiArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/bungee/src/main/java/me/googas/commands/bungee/providers/type/BungeeMultiArgumentProvider.java)
* JDA: [JdaMultiArgumentProvider](https://github.com/Chevyself/Starbox-Commands/blob/master/jda/src/main/java/me/googas/commands/jda/providers/type/JdaMultiArgumentProvider.java)

```java
public class Test {
    @Command(name = "command")
    public Result command(CommandSender sender, @Required String name, @Optional long size, @Multiple @Optional JoinedStrings args) {
        return new Result();
    }
}            
```    

Registering commands
--------

1. Creating the Command Manager. Each `CommandManager` has its own options.

```java
CommandManager manager = new CommandManager(
        // Options of the command manager
        );
```

2. You can either create a command extending the class in the module that implements `StarboxCommand` or using `CommandManager#parseCommand(Object)` which will get the commands using 
reflection as shown in [creating a command](#creating-a-command)

```java
manager.register(manager.parseCommand(cmd));
// or
manager.parseAndRegister(cmd);
```

4. You are done! You've registered your commands.