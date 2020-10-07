package com.starfishst.jda.annotations;

import com.starfishst.jda.ManagerOptions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This tells the {@link com.starfishst.jda.CommandManager} that if a command is annotated with
 * {@link Exclude} and {@link ManagerOptions#isDeleteSuccess()} is set to true, the message should
 * not be deleted
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Exclude {}
