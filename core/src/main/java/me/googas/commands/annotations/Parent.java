package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.googas.commands.EasyCommand;

/**
 * Used to represent the parent commands that are parsed using reflection
 *
 * <p>Lets suppose to have the prefix '/'. If you have the {@link me.googas.commands.EasyCommand}
 * 'hello' you can parseAndRegister other {@link me.googas.commands.EasyCommand} using {@link
 * me.googas.commands.EasyCommand#addChildren(EasyCommand)} and the command execution with be as
 * follows:
 *
 * <p>/hello &lt;command&gt; &lt;**args&gt;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parent {}
