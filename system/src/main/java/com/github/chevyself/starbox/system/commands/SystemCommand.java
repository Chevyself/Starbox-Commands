package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.commands.Command;
import com.github.chevyself.starbox.system.context.CommandContext;

/**
 * This is the direct implementation of {@link Command} for the "System" module. Extending
 * this class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(SystemCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link ReflectSystemCommand}
 *
 * <p>To parse {@link ReflectSystemCommand} is required to use the annotation {@link Command} if you
 * would like to create an extension the method to override is {@link #run(CommandContext)}
 */
public interface SystemCommand extends Command<CommandContext, SystemCommand> {

}
