package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.metadata.CommandMetadata;

public class EmptyCommandMetadataParser implements CommandMetadataParser {

  @Override
  public CommandMetadata parse() {
    return new CommandMetadata();
  }
}
