package com.starfishst.util;

/** Test for raytrace */
public class RayTraceTest {

  public static void main(String[] args) {
    double minecraftYaw = 90;
    double minecraftPitch = 0;
    double yaw = convertToRadians(minecraftYaw);
    double pitch = convertToRadians(minecraftPitch);
    System.out.println(yaw);
    System.out.println(pitch);
    double x = Math.sin(pitch) * Math.cos(yaw);
    double y = Math.cos(pitch);
    double z = Math.sin(pitch) * Math.sin(yaw);
    System.out.println(x);
    System.out.println(y);
    System.out.println(z);
  }

  public static double convertToRadians(double minecraftDegrees) {
    return ((minecraftDegrees) + 90) * Math.PI / 180;
  }
}
