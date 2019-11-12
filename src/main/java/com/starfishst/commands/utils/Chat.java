package com.starfishst.commands.utils;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Chat {

  public static void send(
      @NotNull CommandSender sender, @NotNull Object message, Object... strings) {
    sender.sendMessage(Strings.getMessage(message, strings));
  }

  public static <O> String paginate(int page, int limit, List<O> list) {
    int maxPage = (list.size() + limit - 1) / limit;
    StringBuilder builder = Strings.getBuilder();

    if (page <= 0) {
      throw new IllegalArgumentException("Pages start from 1");
    }
    if (page > maxPage) {
      throw new IllegalArgumentException("Page out of bounds!");
    } else {
      for (int i = (page - 1) * limit; i < ((page - 1) * limit) + (limit); i++) {
        if (i > list.size() - 1) {
          break;
        } else {
          final O o = list.get(i);
          builder.append(o.toString()).append("\n");
        }
      }
    }
    String PAGINATION = "&7Page &8{0} &7of &8{1} \n {2}";
    return Strings.getMessage(PAGINATION, page, maxPage, builder.toString());
  }
}
