package com.starfishst.util;

import com.starfishst.core.utils.math.MathUtils;
import com.starfishst.core.utils.math.geometry.Box;
import com.starfishst.core.utils.math.geometry.Point;

/** This is a test for calculating distance between a point and shapes */
public class DistanceTest {

  public static void main(String[] args) {
    Box box = new Box(new Point(30, 30, 30), new Point(50, 50, 50), null);
    Point u = new Point(3, -4, 5);
    double distanceX =
        Math.max(box.getMinimum().getX() - u.getX(), u.getX() - box.getMaximum().getX());
    double distanceY =
        Math.max(box.getMinimum().getY() - u.getY(), u.getY() - box.getMaximum().getY());
    double distanceZ =
        Math.max(box.getMinimum().getZ() - u.getZ(), u.getZ() - box.getMaximum().getZ());
    Point minimum = new Point(distanceX, distanceY, distanceZ);
    System.out.println(minimum);
    System.out.println(distanceX);
    System.out.println(distanceY);
    System.out.println(distanceZ);
    double distance =
        Math.sqrt(
            MathUtils.square(distanceX)
                + MathUtils.square(distanceY)
                + MathUtils.square(distanceZ));
    System.out.println(distance);
    System.out.println(u.distance(minimum));
  }
}
