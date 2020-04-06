package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class DoubleProvider implements IArgumentProvider<Double> {

    @Override
    public @NotNull Class<Double> getClazz() {
        return double.class;
    }

    @NotNull
    @Override
    public Double fromString(@NotNull String string, @NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            throw new ArgumentProviderException("{0} is not the correct format for double", string);
        }
    }
}
