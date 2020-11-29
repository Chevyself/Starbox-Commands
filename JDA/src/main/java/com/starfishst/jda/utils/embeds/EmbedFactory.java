package com.starfishst.jda.utils.embeds;

import com.starfishst.jda.ManagerOptions;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.listener.CommandListener;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.result.Result;
import com.starfishst.jda.result.ResultType;
import java.awt.*;
import java.util.LinkedHashMap;
import lombok.NonNull;
import me.googas.commons.Validate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/** Easily create embeds */
public class EmbedFactory {

  /** The embed builder */
  private static final EmbedBuilder embedBuilder = new EmbedBuilder();

  /**
   * Create an embed using a result and some message options
   *
   * @param result the result to make as embed
   * @param listener the command listener to get the {@link ManagerOptions}
   * @param context the context of the command
   * @return an embed query built from the result
   */
  public static EmbedQuery fromResult(
      @NonNull Result result, @NonNull CommandListener listener, CommandContext context) {
    MessagesProvider messagesProvider = listener.getMessagesProvider();
    ManagerOptions managerOptions = listener.getManagerOptions();
    ResultType type = result.getType();
    final String thumbnail = messagesProvider.thumbnailUrl(context);
    return newEmbed(
        type.getTitle(messagesProvider, context),
        result.getMessage(),
        thumbnail,
        null,
        messagesProvider.footer(context),
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
      String title,
      String description,
      String thumbnail,
      String image,
      String footer,
      Color color,
      LinkedHashMap<String, String> fields,
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
  public static @NonNull EmbedBuilder getEmbedBuilder() {
    return EmbedFactory.embedBuilder.clear();
  }
}
