package com.starfishst.core.utils.math.geometry.containers;

import com.starfishst.core.utils.math.geometry.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Contains an infinite amount of points */
public class InfinitePoints extends Points {

  /** Create the collection of points */
  public InfinitePoints() {
    super(new ArrayList<>());
  }

  /**
   * Get the contained points
   *
   * @return the points
   */
  @NotNull
  @Override
  public List<Point> getPoints() {
    throw new UnsupportedOperationException(
        "There's infinite points. This operation would never end");
  }

  /**
   * Get whether the contained points are infinite
   *
   * @return true if there's an infinite amount of points
   */
  @Override
  public boolean isInfinite() {
    return true;
  }

  /**
   * Get the size of the points
   *
   * @return the size of the points
   */
  @Override
  public int size() {
    return Integer.MAX_VALUE;
  }

  /**
   * Get whether there's contained points
   *
   * @return true if there's no points inside
   */
  @Override
  public boolean isEmpty() {
    return false;
  }

  /**
   * Get whether a point is inside or not
   *
   * @param point the point to check if it is inside
   * @return true if this contains the point
   */
  @Override
  public boolean contains(@Nullable Point point) {
    return true;
  }

  /**
   * Adds a point to the set of points
   *
   * @param point the point to add
   * @return whether or not the point was added
   */
  @Override
  public boolean add(@NotNull Point point) {
    return false;
  }

  /**
   * Removes a point from the set
   *
   * @param point the point to remove from the set
   * @return whether or not the point was removed
   */
  @Override
  public boolean remove(@Nullable Point point) {
    return false;
  }

  /**
   * Checks if a collection of points is contained inside the set
   *
   * @param collection the collection of points
   * @return true if the collection of points is contained
   */
  @Override
  public boolean containsAll(@NotNull Collection<? extends Point> collection) {
    return true;
  }

  /**
   * Adds a collection of points to the set
   *
   * @param collection the collection to add
   * @return true if the collection was added
   */
  @Override
  public boolean addAll(@NotNull Collection<? extends Point> collection) {
    return false;
  }

  /**
   * Removes all the points that are not contained in another collection of points
   *
   * @param collection the collection of points
   * @return true if the set changed
   */
  @Override
  public boolean retainAll(@NotNull Collection<? extends Point> collection) {
    return false;
  }

  /**
   * Removes all the points contained inside another collection
   *
   * @param collection the points to remove from the set
   * @return true if there was changes
   */
  @Override
  public boolean removeAll(@NotNull Collection<? extends Point> collection) {
    return false;
  }

  /** Clears the set of points */
  @Override
  public void clear() {
    super.clear();
  }

  /**
   * Removes from the set the points which assert the predicate
   *
   * @param filter the filter which the set should assert
   * @return true if there was changes made
   */
  @Override
  public boolean removeIf(@NotNull Predicate<? super Point> filter) {
    return false;
  }

  /**
   * Creates an stream of points
   *
   * @return the stream of points
   */
  @Override
  public Stream<Point> stream() {
    throw new UnsupportedOperationException(
        "There's infinite points. This operation would never end");
  }

  /**
   * Makes each points do a different action
   *
   * @param action the action for each point
   */
  @Override
  public void forEach(Consumer<? super Point> action) {
    throw new UnsupportedOperationException(
        "There's infinite points. This operation would never end");
  }

  /**
   * Adds all the points inside another set of points
   *
   * @param points the points to add
   * @return true if there where changed made
   */
  @Override
  public boolean addAll(@NotNull Points points) {
    return false;
  }

  /**
   * Get the smallest point
   *
   * <p>The default value is a point x=0, y=0, z=0
   *
   * @return the smallest point
   */
  @Override
  public @NotNull Point getMinimum() {
    return new InfinitePoint(true);
  }

  /**
   * Get the biggest point
   *
   * <p>The default value is a point x=0, y=0, z=0
   *
   * @return the biggest point
   */
  @Override
  public @NotNull Point getMaximum() {
    return new InfinitePoint(false);
  }
}
