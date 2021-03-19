import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.result.Result;

public class SimpleCommandSample {

  @Command(aliases = "Hello")
  public Result aCommand() {
    return new Result("World!");
  }
}
