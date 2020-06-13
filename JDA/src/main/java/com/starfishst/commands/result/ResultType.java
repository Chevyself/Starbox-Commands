package com.starfishst.commands.result;

import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import java.awt.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The different types of results */
public enum ResultType {
  /** An error result */
  ERROR("**Error**:", true),
  /** An unknown error result */
  UNKNOWN("**Error**: → Unknown error", true),
  /** An usage error result */
  USAGE("**Error**: → Wrong usage", true),
  /** A normal result */
  GENERIC("Success!", false),
  /** An error permission result */
  PERMISSION("**Error**: → No permission", true);

  /** The tile of the type for messages */
  @NotNull private final String title;
  /** Whether the result is an error */
  private final boolean isError;

  ResultType(@NotNull String title, boolean isError) {
    this.title = title;
    this.isError = isError;
  }

  /**
   * Get the title of the result
   *
   * @param provider in case that there's provider use it to get the title
   * @param context the context of the command
   * @return the title
   */
  @NotNull
  public String getTitle(@Nullable MessagesProvider provider, @Nullable CommandContext context) {
    if (provider != null && context != null) return provider.getTitle(this, context);
    return title;
  }

  /**
   * Get the color that represents the result. It applies when {@link
   * ManagerOptions#isEmbedMessages()} is true
   *
   * @param options the manager options to get the color from
   * @return the color
   */
  @NotNull
  public Color getColor(@NotNull ManagerOptions options) {
    if (isError) {
      return options.getError();
    } else {
      return options.getSuccess();
    }
  }

  /**
   * Get if the result is an error
   *
   * @return true if is an error
   */
  public boolean isError() {
    return isError;
  }
}
