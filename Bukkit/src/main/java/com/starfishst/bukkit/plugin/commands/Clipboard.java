package com.starfishst.bukkit.plugin.commands;

import com.starfishst.core.utils.math.geometry.Point;
import org.jetbrains.annotations.Nullable;

public class Clipboard {

  /** The first position of the clipboard */
  @Nullable private Point pos1;
  /** The second position of the clipboard */
  @Nullable private Point pos2;

  /**
   * Create the clipboard
   *
   * @param pos1 the first position of the clipboard
   * @param pos2 the second position of the clipboard
   */
  public Clipboard(@Nullable Point pos1, @Nullable Point pos2) {
    this.pos1 = pos1;
    this.pos2 = pos2;
  }

  /**
   * Set the first position of the clipboard
   *
   * @param pos1 the new first position
   */
  public void setPos1(@Nullable Point pos1) {
    this.pos1 = pos1;
  }

  /**
   * Set the second position of the clipboard
   *
   * @param pos2 the new second position
   */
  public void setPos2(@Nullable Point pos2) {
    this.pos2 = pos2;
  }

  /**
   * Get the first position of the clipboard
   *
   * @return the first position of the clipboard
   */
  @Nullable
  public Point getPos1() {
    return pos1;
  }

  /**
   * Get the second position of the clipboard
   *
   * @return the second position
   */
  @Nullable
  public Point getPos2() {
    return pos2;
  }
}
