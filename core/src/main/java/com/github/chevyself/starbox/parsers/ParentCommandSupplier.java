package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

public interface ParentCommandSupplier<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull
  T supply(@NonNull Command annotation, @NonNull Class<?> clazz);
}
