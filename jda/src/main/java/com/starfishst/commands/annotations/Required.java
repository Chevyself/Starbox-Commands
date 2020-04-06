package com.starfishst.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jetbrains.annotations.NotNull;

/**
 * This marks a parameter as required. It can be think in a way as {@link
 * org.jetbrains.annotations.NotNull}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Required {

    /**
     * @return The name of the argument
     */
    @NotNull
    String name() default "No name provided";

    /**
     * @return The description of the argument
     */
    @NotNull
    String description() default "No description provided";

    /**
     * @return The suggestions to be shown for the argument
     */
    @NotNull
    String[] suggestions() default {};
}
