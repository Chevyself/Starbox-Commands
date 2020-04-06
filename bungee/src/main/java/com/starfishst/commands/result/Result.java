package com.starfishst.commands.result;

import com.starfishst.core.result.SimpleResult;
import org.jetbrains.annotations.NotNull;

public class Result extends SimpleResult {

    /**
     * Create a new instance of {@link SimpleResult} with a message with place holders
     *
     * @param string  the message with place holders
     * @param strings the place holders
     */
    public Result(@NotNull String string, Object... strings) {
        super(string, strings);
    }

    public Result() {
        super();
    }
}
