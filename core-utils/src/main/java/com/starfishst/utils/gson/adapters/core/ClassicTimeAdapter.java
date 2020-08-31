package com.starfishst.utils.gson.adapters.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.starfishst.core.utils.time.ClassicTime;
import com.starfishst.core.utils.time.Time;
import java.lang.reflect.Type;

/** The adapter for classic time */
public class ClassicTimeAdapter
    implements JsonSerializer<ClassicTime>, JsonDeserializer<ClassicTime> {

  @Override
  public JsonElement serialize(
      ClassicTime classicTime, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(classicTime.toTime().toDatabaseString());
  }

  @Override
  public ClassicTime deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return Time.fromString(jsonElement.getAsString()).toClassicTime();
  }
}
