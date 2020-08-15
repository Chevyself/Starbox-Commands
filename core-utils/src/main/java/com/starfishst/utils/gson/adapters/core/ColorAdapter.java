package com.starfishst.utils.gson.adapters.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.awt.*;
import java.lang.reflect.Type;

public class ColorAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
  @Override
  public JsonElement serialize(
      Color color, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(
        String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
  }

  @Override
  public Color deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return new Color(Integer.decode(jsonElement.getAsString()));
  }
}
