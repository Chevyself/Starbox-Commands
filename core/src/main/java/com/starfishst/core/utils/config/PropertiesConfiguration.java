package com.starfishst.core.utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A configuration made with java properties */
public class PropertiesConfiguration implements Configuration {

  /** The file of the configuration */
  @NotNull private final File file;
  /** The properties instance to get and set values */
  @NotNull private final Properties properties;

  /**
   * Create the properties configuration
   *
   * @param file the file of the configuration
   * @param properties the properties of the configuration
   */
  public PropertiesConfiguration(@NotNull File file, @NotNull Properties properties) {
    this.file = file;
    this.properties = properties;
  }

  public PropertiesConfiguration(@NotNull File file, @Nullable InputStream defaultResource)
      throws IOException {
    this.file = file;
    this.properties = new Properties();
    FileInputStream inputStream = new FileInputStream(file);
    this.properties.load(new FileInputStream(file));
    inputStream.close();
    if (defaultResource != null) {
      copyDefaults(defaultResource);
    }
  }

  /**
   * Copies the default values of the missing keys
   *
   * @param defaultResource the default resource as input stream
   * @throws IOException in case that the properties cannot be loaded from the default resource
   */
  private void copyDefaults(@NotNull InputStream defaultResource) throws IOException {
    Properties def = new Properties();
    def.load(defaultResource);
    def.stringPropertyNames()
        .forEach(
            key -> {
              if (this.properties.get(key) == null) {
                this.properties.setProperty(key, def.getProperty(key));
              }
            });
  }

  /**
   * Saves the configuration into the file
   *
   * @param comments comments to add when saving it
   * @throws IOException in case something goes wrong
   */
  public void save(@Nullable String comments) throws IOException {
    FileOutputStream stream = new FileOutputStream(file);
    properties.store(stream, comments);
    stream.close();
  }

  /**
   * Get the properties instance of the resource
   *
   * @return the properties instance
   */
  @NotNull
  public Properties getProperties() {
    return properties;
  }

  @Override
  public @NotNull File getFile() {
    return this.file;
  }

  /**
   * Saves the configuration into the file
   *
   * @throws IOException in case something goes wrong
   */
  @Override
  public void save() throws IOException {
    save(null);
  }
}
