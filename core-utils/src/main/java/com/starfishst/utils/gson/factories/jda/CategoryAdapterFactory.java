package com.starfishst.utils.gson.factories.jda;

import com.starfishst.utils.gson.factories.InterfaceTypeAdapterFactory;
import net.dv8tion.jda.api.entities.Category;
import org.jetbrains.annotations.NotNull;

public class CategoryAdapterFactory implements InterfaceTypeAdapterFactory<Category> {
  /**
   * Get the class of the interface
   *
   * @return the class of the interface
   */
  @Override
  public @NotNull Class<Category> getClazz() {
    return Category.class;
  }
}
