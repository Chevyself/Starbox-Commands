package com.starfishst.utils.gson.adapters.jda;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.starfishst.core.fallback.Fallback;
import com.starfishst.core.utils.time.Time;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.JDAImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An adapter for creating a jda connection from a json configuration */
public class JdaAdapter implements JsonSerializer<JDAImpl>, JsonDeserializer<JDA> {

  /** A logger used to print messages */
  @Nullable private final Logger logger;
  /** The intents that the jda api should use */
  @NotNull private final Collection<GatewayIntent> intents;
  /** The type of event manager that the instance of jda should use */
  @NotNull private final IEventManager eventManager;
  /** The listeners that the instance of jda should use */
  @NotNull private final Collection<Object> listeners;
  /** The token used to connect to the bot */
  @NotNull private static String token = "";

  /**
   * Create the adapter
   *
   * @param logger the logger to print information messages
   * @param intents the intents that the jda api should use
   * @param eventManager the type of event manager that the jda instance should use
   * @param listeners the listeners that jda should use
   */
  public JdaAdapter(
      @Nullable Logger logger,
      @NotNull Collection<GatewayIntent> intents,
      @NotNull IEventManager eventManager,
      @NotNull Collection<Object> listeners) {
    this.logger = logger;
    this.intents = intents;
    this.eventManager = eventManager;
    this.listeners = listeners;
  }

  /**
   * Send an information message
   *
   * @param message the message to send
   */
  private void info(@NotNull String message) {
    if (this.logger != null) {
      this.logger.info(message);
    }
  }

  /**
   * Send an exception
   *
   * @param throwable the exception to send
   * @param error a custom fallback error if this is null it will use the throwable message
   */
  private void exception(@NotNull Throwable throwable, @Nullable String error) {
    Fallback.addError(error == null ? throwable.getMessage() : error);
    if (this.logger != null) {
      this.logger.log(Level.SEVERE, throwable.getMessage(), throwable);
    } else {
      throwable.printStackTrace();
    }
  }

  @Override
  @NotNull
  public JsonElement serialize(
      JDAImpl jda, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject object = new JsonObject();
    object.addProperty("token", token);
    return object;
  }

  @Override
  @Nullable
  public JDA deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    try {
      JsonObject object = jsonElement.getAsJsonObject();
      token = object.get("token").getAsString();
      JDA jda =
          JDABuilder.create(token, this.intents)
              .setEventManager(this.eventManager)
              .addEventListeners(this.listeners)
              .build();
      long millis = 0;
      this.info("Waiting for discord connection");
      while (jda.getStatus() != JDA.Status.CONNECTED) {
        try {
          Thread.sleep(1);
          millis++;
        } catch (InterruptedException e) {
          exception(e, "Interrupted while trying to connect to discord");
        }
        if (millis > 2000) {
          jda = null;
          this.info("Discord could not be connected after 2 seconds");
          break;
        }
      }
      if (jda != null) {
        this.info("Discord took " + Time.fromMillis(millis).toEffectiveString() + " to connect");
      }
      return jda;
    } catch (LoginException e) {
      exception(e, null);
      return null;
    }
  }
}
