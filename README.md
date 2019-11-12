# Easy-Commands
Minecraft command framework for Spigot or Bukkit 1.14!

## Installing

As there's no maven repository you must install the framework manually.

1. Clone the repository.

```
git clone https://github.com/xChevy/Easy-Commands.git
```

2. Enter the repository directory.

```
cd Easy-Commands
```

3. Run maven to copy the framework to your ./m2

```
mvn clean install
```

4. Add the dependency to your project pom.xml.

```
<dependency>
  <artifactId>EasyCommands</artifactId>
  <groupId>com.starfishst.commands</groupId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
5. Read the documentation and create your own commands!

## Running

1. Create the [Command Manager](https://github.com/xChevy/Easy-Commands/blob/master/src/main/java/com/starfishst/commands/CommandManager.java) instance.

```java
public class Main extends JavaPlugin {
  @Override
  public void onEnable() {
    // Console is a util from the framework that can be used wherever in the plugin.
    // It is used to display errors and information by default.
    new Console(this);
    Console.info("Loading commands");
    
    final CommandManager commandManager = new CommandManager(this);
    // This method adds the plugin to the Bukkit help map
    commandManager.registerPlugin();
  }
}
```
2. Make and register your first command.

``` java
public class TestCommand {
  @Command(aliases = "test")
  public Result test(){
    // When executed the command will send a "Hello world!" message to the sender
    return new Result("Hello world!");
  }
}
```  
In your [Command Manager](https://github.com/xChevy/Easy-Commands/blob/master/src/main/java/com/starfishst/commands/CommandManager.java) instance register the command.

``` java
commandManager.registerCommands(new TestCommand());
```

> Register your commands before the plugin so that they can be added to the help topic

> The method in the class TestCommand can be static.

## The [@Command](https://github.com/xChevy/Easy-Commands/blob/master/src/main/java/com/starfishst/commands/annotations/Command.java) annotation

- Aliases: This is required because the first alias will be the name of the command
> This are not required
- Description: A simple description to tell the player what the command is for
- Usage: How the command must be executed. Generally <> means required and () optional
- Permission: Don't let all players to use the command adding a node

Example: 

```java
@Command(
      aliases = {"test", "tst"},
      description = "Say hello to the world",
      usage = "",
      permission = "test.hello")
```

## Create a parent command.
What does parent mean? This means that a command will hold other commands on it. How?

This is the command test: /test 0

In the position 0 we will have a String that will be used as a subcommand: `/test debug` and `/test test`.

In code:

```java
public class TestCommand {
  @Parent
  @Command(aliases = "test")
  public Result test(){
    return new Result("Use a subcommand such as debug or test!");
  }

  @Command(aliases = "debug")
  public Result debug(){
    return new Result("This will be run when the command &o/test debug &ris sent!");
  }
  // Even tho this command has the same alias as parent it will still run as the first one's
  // got the @Parent annotation
  @Command(aliases = "test")
  public Result testSubcommand(){
    return new Result("Oh another subcommand!");
  }
}
```

The [@Command](https://github.com/xChevy/Easy-Commands/blob/master/src/main/java/com/starfishst/commands/annotations/Command.java) annotation will work just as normal.
