package com.starfishst.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for the representation of parameters that require multiple strings
 *
 * <p>An object that needs multiple stings can be something like an array. For example: The custom
 * object {@link com.starfishst.core.objects.JoinedStrings}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Multiple {}
