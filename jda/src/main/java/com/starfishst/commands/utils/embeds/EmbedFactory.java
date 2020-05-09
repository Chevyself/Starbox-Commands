package com.starfishst.commands.utils.embeds;

import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.listener.CommandListener;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.utils.Validate;
import java.awt.*;
import java.util.LinkedHashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Easily create embeds */
public class EmbedFactory {

  /** The embed builder */
  private static final EmbedBuilder embedBuilder = new EmbedBuilder();

  /**
   * Create an embed using a result and some message options
   *
   * @param result the result to make as embed
   * @param listener the command listener to get the {@link ManagerOptions}
   * @return an embed query built from the result
   */
  public static EmbedQuery fromResult(@NotNull Result result, @NotNull CommandListener listener) {
    MessagesProvider messagesProvider = listener.getMessagesProvider();
    ManagerOptions managerOptions = listener.getManagerOptions();
    ResultType type = result.getType();
    final String thumbnail = messagesProvider.thumbnailUrl();
    return newEmbed(
        type.getTitle(messagesProvider),
        result.getMessage(),
        thumbnail,
        null,
        messagesProvider.footer(),
        type.getColor(managerOptions),
        null,
        false);
  }

  /**
   * Create an embed
   *
   * @param title the title of the embed
   * @param description the description of the embed
   * @param thumbnail the thumbnail of the embed
   * @param image the image of the embed
   * @param footer the footer of the embed
   * @param color the color of the embed
   * @param fields the fields of the embed (Leave value in blank for blank field)
   * @param inline should the fields be inline
   * @return a built query
   */
  public static EmbedQuery newEmbed(
      @Nullable String title,
      @Nullable String description,
      @Nullable String thumbnail,
      @Nullable String image,
      @Nullable String footer,
      @Nullable Color color,
      @Nullable LinkedHashMap<String, String> fields,
      boolean inline) {
    EmbedBuilder builder = getEmbedBuilder();
    if (title != null) {
      Validate.assertFalse(
          title.length() >= MessageEmbed.TITLE_MAX_LENGTH,
          "Title cannot be longer than " + MessageEmbed.TITLE_MAX_LENGTH);
      builder.setTitle(title);
    }
    if (description != null) {
      Validate.assertFalse(
          description.length() >= MessageEmbed.TEXT_MAX_LENGTH,
          "Description cannot be longer than " + MessageEmbed.TEXT_MAX_LENGTH);
      builder.setDescription(description);
    }
    if (thumbnail != null && !thumbnail.isEmpty()) {
      Validate.assertFalse(
          thumbnail.length() >= MessageEmbed.URL_MAX_LENGTH,
          "URL cannot be longer than " + MessageEmbed.URL_MAX_LENGTH);
      builder.setThumbnail(thumbnail);
    }
    if (image != null) {
      builder.setImage(image);
      Validate.assertFalse(
          image.length() >= MessageEmbed.URL_MAX_LENGTH,
          "URL cannot be longer than " + MessageEmbed.URL_MAX_LENGTH);
    }
    if (footer != null) {
      Validate.assertFalse(
          footer.length() >= MessageEmbed.TEXT_MAX_LENGTH,
          "Footer cannot be longer than " + MessageEmbed.TEXT_MAX_LENGTH);
      builder.setFooter(footer);
    }
    if (color != null) builder.setColor(color);
    if (fields != null) {
      fields.forEach(
          (key, value) -> {
            Validate.assertFalse(
                key.length() >= MessageEmbed.TITLE_MAX_LENGTH,
                key + " cannot be longer than " + MessageEmbed.TITLE_MAX_LENGTH);
            Validate.assertFalse(
                value.length() >= MessageEmbed.VALUE_MAX_LENGTH,
                value + " cannot be longer than " + MessageEmbed.VALUE_MAX_LENGTH);
            if (value.isEmpty()) {
              builder.addBlankField(inline);
            } else {
              builder.addField(key, value, inline);
            }
          });
    }
    Validate.assertFalse(
        builder.length() >= MessageEmbed.EMBED_MAX_LENGTH_BOT,
        "This embed is too big! Max: "
            + MessageEmbed.EMBED_MAX_LENGTH_BOT
            + " characters and it has "
            + embedBuilder.length());
    return new EmbedQuery(embedBuilder);
  }

  /**
   * Get the embed builder used in the factory. (It is empty)
   *
   * @return the embed builder used
   */
  public static @NotNull EmbedBuilder getEmbedBuilder() {
    return EmbedFactory.embedBuilder.clear();
  }
}
