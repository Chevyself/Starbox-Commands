package com.starfishst.utils.gson.adapters.core.math.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.starfishst.core.utils.Strings;
import com.starfishst.core.utils.math.geometry.Point;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;

/** Adapts a point into json */
public class PointAdapter implements JsonSerializer<Point>, JsonDeserializer<Point> {

  /**
   * Get the json format of a double
   *
   * @param number the double to get as json
   * @return the json object
   */
  @NotNull
  public static String infiniteToJson(double number) {
    return number < 0 ? "-oo" : "oo";
  }

  /**
   * Get a double from a json element
   *
   * @param element the json element to get the double from
   * @return the double parsed from the json file
   */
  public static double fromJson(@NotNull JsonElement element) {
    String string = element.getAsString();
    if (Strings.containsIgnoreCase(string, "oo")) {
      return string.startsWith("-") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    } else {
      return element.getAsDouble();
    }
  }

  @Override
  public JsonElement serialize(
      Point point, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject object = new JsonObject();
    if (Double.isInfinite(point.getX())) {
      object.addProperty("x", infiniteToJson(point.getX()));
    } else {
      object.addProperty("x", point.getX());
    }
    if (Double.isInfinite(point.getY())) {
      object.addProperty("y", infiniteToJson(point.getY()));
    } else {
      object.addProperty("y", point.getY());
    }
    if (Double.isInfinite(point.getZ())) {
      object.addProperty("z", infiniteToJson(point.getZ()));
    } else {
      object.addProperty("z", point.getZ());
    }
    return null;
  }

  @Override
  public Point deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    JsonObject object = jsonElement.getAsJsonObject();
    return new Point(
        fromJson(object.get("x")), fromJson(object.get("z")), fromJson(object.get("y")));
  }
}
