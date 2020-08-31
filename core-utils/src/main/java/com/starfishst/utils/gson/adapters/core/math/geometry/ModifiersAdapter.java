package com.starfishst.utils.gson.adapters.core.math.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.starfishst.core.utils.math.geometry.modifier.Complement;
import com.starfishst.core.utils.math.geometry.modifier.Intersect;
import com.starfishst.core.utils.math.geometry.modifier.Modifier;
import com.starfishst.core.utils.math.geometry.modifier.Negative;
import com.starfishst.core.utils.math.geometry.modifier.Union;
import java.lang.reflect.Type;

/** Adapts the modifier class to json */
public class ModifiersAdapter implements ModifierAdapter<Modifier> {

  @Override
  public JsonElement serialize(Modifier modifier, Type type, JsonSerializationContext context) {
    return context.serialize(modifier);
  }

  @Override
  public Modifier deserialize(
      JsonElement jsonElement, Type aType, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject object = jsonElement.getAsJsonObject();
    if (object.get("shapes") != null) {
      if (object.get("shape") != null) {
        return context.deserialize(object, Complement.class);
      } else {
        JsonElement element = object.get("type");
        if (element != null) {
          String type = element.getAsString();
          if (type.equalsIgnoreCase("intersect")) {
            return context.deserialize(object, Intersect.class);
          } else if (type.equalsIgnoreCase("negative")) {
            return context.deserialize(object, Negative.class);
          } else if (type.equalsIgnoreCase("union")) {
            return context.deserialize(object, Union.class);
          } else {
            throw new JsonParseException(
                "Your input does not match a known modifier in: " + object);
          }
        } else {
          throw new JsonParseException("Modifier is missing the element 'type' in: " + object);
        }
      }
    } else {
      throw new JsonParseException("Modifier is missing the element 'shapes' in: " + object);
    }
  }
}
