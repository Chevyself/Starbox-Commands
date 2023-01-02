Starbox-Commands [![](https://jitpack.io/v/Chevyself/starbox-commands.svg)](https://jitpack.io/#Chevyself/starbox-commands)
===

This project is an abstract interface extended for the use of Bukkit, Bungeecord and JDA. It can also be extended to any platform.

Documentation and Installation
--------

* Check latest JavaDoc in [Jitpack](https://jitpack.io/com/github/chevyself/starbox-commands/master-SNAPSHOT/javadoc/)
* You can find tutorials in the [Wiki](https://github.com/Chevyself/Starbox-commands/wiki).
* Installation steps can be found in [Jitpack](https://jitpack.io/com/github/chevyself/starbox-commands/master-SNAPSHOT/javadoc/) 

---

Creating the Command Manager
--------

Each module has implemented `CommandManager`, thus, making the constructor of the class different in each. While in Bukkit and Bungee you need the `plugin`, in JDA you need the `JDA` object. The manager depends in two classes: the [ProvidersRegistry](./wiki/Providers-Registry) which handles argument providers and the [MessagesProvider](./wiki/Messages-Provider) which provides messages.

Bukkit and Bungee example:
```java
public class Main extends JavaPlugin {
    private CommandManager commandManager;
    
    @Override
    public void onEnable() {
        ProvidersRegistry<> providersRegistry = ...;
        MessagesProvider messagesProvider = ...;
        this.commandManager = new CommandManager(this, providersRegistry, messagesProvider);
    }
}
```

The JDA module also depends in the `ListenerOptions` which handles commands pre-execution, any kind of error, prefixes and the result of slash commands. You could use the default behaviour using `GenericListenerOptions` or create your own class extending `ListenerOptions`.

JDA example:
```java
public class Main {
    public static void main(String[] args) {
        ProvidersRegistry<> providersRegistry = ...;
        MessagesProvider messagesProvider = ...;
        JDA jda = ...;
        ListenerOptions listenerOptions = ...;
        CommandManager commandManager = new CommandManager(providersRegistry, messagesProvider, jda, listenerOptions);
    }
}
```

### Middlewares

Once you got your manager, you can add [middlewares](./wiki/Middlewares). These will change how the command is executed, for example, you can add a middleware to check if the sender has a permission or if the command is executed in a specific channel. You can also handle the result, for example, you can add a middleware to send a message to the sender if the command was executed successfully.

Example:
```java
CommandManager manager = ...;
// Adds a global middleware
manager.addGlobalMiddleware(middleware);
// Adds many global middlewares
manager.addGlobalMiddlewares(middleware1, middleware2, middleware3);
// Adds a middleware
manager.addMiddleware(middleware);
// Adds many middlewares
manager.addMiddlewares(middleware1, middleware2, middleware3);
```

### Registering commands

Once you got your manager ready, you can register your commands. You can either implement the command class:

* Bukkit -> StarboxBukkitCommand
* Bungee -> BungeeCommand
* JDA -> JdaCommand

Or [parse commands using reflection](./wiki/Reflection-Commands).

Example:
```java
List<AnnotatedCommands> commands = commandManager.parseCommands(new MyCommand());
```

When you have your commands ready, you can register them:

```java
// Registers a single command
commandManager.register(command);
// Registers many commands
commandManager.registerAll(command1, command2, command3);
// Register many commands in a collection
List<? extends StarboxCommand> commands = ...;
commandManager.registerAll(commands);
```

## Close the manager

Managers have a `close()` method which will close the manager and unregister all commands.

Example:
```java
manager.close();
```

---

Dependencies
--------

You are adviced to view the platforms used in the framework.

* [Spigot](https://hub.spigotmc.org/)
* [BungeeCord](https://github.com/SpigotMC/BungeeCord)
* [JDA](https://github.com/DV8FromTheWorld/JDA)

Contribution
--------
To contribute, simply, branch out of the `master` branch and create a pull request. If you want to add a new feature, please, create an issue first.

Deprecated content will be removed in the next major release.

Subject to change:
* [ ] The `@Command` annotation of each module may join in a single one.
* [ ] The `CommandManager` of each module may join in a single one.
* [ ] An `Adapter` interface may be introduced to handle the differences between each module.

All the changes above are in favor to reduce duplicated code and make it easier to maintain.

License
--------

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details