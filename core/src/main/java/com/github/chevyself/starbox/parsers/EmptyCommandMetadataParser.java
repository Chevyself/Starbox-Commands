package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.metadata.CommandMetadata;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

public class EmptyCommandMetadataParser implements CommandMetadataParser {
  @Override
  public @NonNull CommandMetadata parse(@NonNull AnnotatedElement element) {
    return new CommandMetadata();
  }
}
