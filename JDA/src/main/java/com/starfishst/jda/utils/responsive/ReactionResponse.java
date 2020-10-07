package com.starfishst.jda.utils.responsive;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

/** This class is the one that executes an action when the unicode matches */
public interface ReactionResponse {

  /**
   * Whether or not the reaction should be removed
   *
   * @return true if it should be removed
   */
  boolean removeReaction();

  /**
   * If unicode matches this is the action to run
   *
   * @param event the reaction event
   */
  void onReaction(@NotNull MessageReactionAddEvent event);

  /**
   * The unicode to match and run the action
   *
   * @return the unicode. It can also be 'any' to accept any kind of emote
   */
  @NotNull
  String getUnicode();
}
