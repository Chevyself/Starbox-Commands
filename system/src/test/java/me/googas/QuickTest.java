package me.googas;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.system.SystemAdapter;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    CommandManager<CommandContext, SystemCommand> manager = new SystemAdapter(args[0]).initialize();
    manager.parseAndRegister(new Commands());
  }
}
