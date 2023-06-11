package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.metadata.CommandMetadata;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

public interface CommandMetadataParser {

  @NonNull
  CommandMetadata parse(@NonNull AnnotatedElement element);
}
