package com.starfishst.utils.gson.adapters.jda;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class UserAdapter extends JdaObjectAdapter<User> {
  /**
   * Create the adapter
   *
   * @param api the api to use for the adaptation
   */
  protected UserAdapter(@NotNull JDA api) {
    super(api);
  }

  @Override
  public JsonElement serialize(
      User user, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(user.getIdLong());
  }

  @Override
  public User deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return api.getUserById(jsonElement.getAsLong());
  }
}
