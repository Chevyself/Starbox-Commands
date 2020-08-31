package com.starfishst.utils.gson.adapters.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.starfishst.core.utils.time.Time;
import java.lang.reflect.Type;

/** The adapter for time */
public class TimeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {

  @Override
  public JsonElement serialize(
      Time time, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(time.toDatabaseString());
  }

  @Override
  public Time deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return Time.fromString(jsonElement.getAsString());
  }
}
