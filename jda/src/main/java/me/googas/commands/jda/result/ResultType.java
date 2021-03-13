package me.googas.commands.jda.result;

import me.googas.commands.jda.ManagerOptions;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import java.awt.*;
import lombok.NonNull;

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
  @NonNull private final String title;
  /** Whether the result is an error */
  private final boolean isError;

  /**
   * The type of result of a command
   *
   * @param title the title of the result
   * @param isError whether this type of result is an error
   */
  ResultType(@NonNull String title, boolean isError) {
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
  @NonNull
  public String getTitle(MessagesProvider provider, CommandContext context) {
    if (provider != null) return provider.getTitle(this, context);
    return title;
  }

  /**
   * Get the color that represents the result. It applies when {@link
   * ManagerOptions#isEmbedMessages()} is true
   *
   * @param options the manager options to get the color from
   * @return the color
   */
  @NonNull
  public Color getColor(@NonNull ManagerOptions options) {
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
