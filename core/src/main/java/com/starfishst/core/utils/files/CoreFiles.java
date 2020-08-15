package com.starfishst.core.utils.files;

import com.starfishst.core.utils.Validate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Core utilities for files */
public class CoreFiles {

  /** The class loader of this class to be used to load resources */
  @NotNull private static final ClassLoader LOADER = CoreFiles.class.getClassLoader();

  /**
   * Get a file from a directory
   *
   * @param parent the directory of the file
   * @param fileName the name of the file
   * @return the file
   */
  @Nullable
  public static File getFile(@Nullable String parent, @NotNull String fileName) {
    File file = new FileNameValidator(parent, fileName).getFile();
    if (file.exists()) {
      return file;
    } else {
      return null;
    }
  }

  /**
   * Get a file
   *
   * @param fileName the path to the file
   * @return the file if found else null
   */
  @Nullable
  public static File getFile(@NotNull String fileName) {
    return getFile(null, fileName);
  }

  /**
   * Gets a file from a directory or creates it
   *
   * @param parent the directory of the file
   * @param fileName the name of the file
   * @return the file
   * @throws IOException if the directory or the file could not be created
   */
  @NotNull
  public static File getOrCreate(@Nullable String parent, @NotNull String fileName)
      throws IOException {
    FileNameValidator validator = new FileNameValidator(parent, fileName);
    File file = validator.getFile();
    File directory = validator.getDirectory();
    if (directory != null && !directory.exists()) {
      if (!directory.mkdir()) {
        throw new IOException("Directory " + directory + " could not be created");
      }
    }
    if (file.exists()) {
      return file;
    } else {
      if (file.createNewFile()) {
        return file;
      } else {
        throw new IOException("The file " + file + " could not be created");
      }
    }
  }

  /**
   * Copies a resource to a file
   *
   * @param file the file to copy the resource to
   * @param stream the resource as stream
   * @return the file
   * @throws IOException if the resource could not be copied
   */
  @NotNull
  public static File copyResource(@NotNull File file, @NotNull InputStream stream)
      throws IOException {
    Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return file;
  }

  /**
   * Copies the resource using the name of the resource
   *
   * @param file the file to copy the resource to
   * @param resourceName the name of the resource
   * @return the file
   * @throws IOException if the file could not be created/copied
   */
  public static File copyResource(@NotNull File file, @NotNull String resourceName)
      throws IOException {
    return copyResource(file, getResource(resourceName));
  }

  /**
   * Get a resource
   *
   * @param name the name of the resource
   * @return the resource as input stream
   */
  @NotNull
  public static InputStream getResource(@NotNull String name) {
    return Validate.notNull(
        LOADER.getResourceAsStream(validatePath(name)), "Resource " + name + " was not found!");
  }

  /**
   * Gets a file or copies a resource
   *
   * @param parent the directory of the file
   * @param fileName the name of the file
   * @param resource the resource
   * @return the file
   * @throws IOException if the file could not be created/copied in case that it is needed
   */
  @NotNull
  public static File getFileOrResource(
      @Nullable String parent, @NotNull String fileName, @NotNull InputStream resource)
      throws IOException {
    File file = getFile(parent, fileName);
    if (file == null || !file.exists()) {
      file = getOrCreate(parent, fileName);
      copyResource(file, resource);
    }
    return file;
  }

  /**
   * Get a file or copy the resource
   *
   * @param fileName the path to the file
   * @param resource the resource
   * @return the file
   * @throws IOException if the file could not be created/copied in case that it is needed
   */
  @NotNull
  public static File getFileOrResource(@NotNull String fileName, @NotNull InputStream resource)
      throws IOException {
    return getFileOrResource(null, fileName, resource);
  }

  /**
   * Gets a file or copies a resource
   *
   * @param parent the directory of the file
   * @param fileName the name of the file
   * @return the file
   * @throws IOException if the file could not be created/copied in case that it is needed
   */
  @NotNull
  public static File getFileOrResource(@Nullable String parent, @NotNull String fileName)
      throws IOException {
    File file = getFile(parent, fileName);
    if (file == null || !file.exists()) {
      file = getOrCreate(parent, fileName);
      copyResource(file, getResource(fileName));
    }
    return file;
  }

  /**
   * Get a file or copy the resource
   *
   * @param fileName the path to the file
   * @return the file
   * @throws IOException if the file could not be created/copied in case that it is needed
   */
  @NotNull
  public static File getFileOrResource(@NotNull String fileName) throws IOException {
    return getFileOrResource(null, fileName, getResource(fileName));
  }

  /**
   * Get a file or create it
   *
   * @param fileName the name of the file
   * @return the file
   * @throws IOException if the file could not be created
   */
  @NotNull
  public static File getOrCreate(@NotNull String fileName) throws IOException {
    return getOrCreate(null, fileName);
  }

  /**
   * Get the current working directory
   *
   * @return the directory
   */
  @NotNull
  public static String currentDirectory() {
    return System.getProperty("user.dir");
  }

  /**
   * Validates the path by changing the '/' to the actual type of separator for each OS
   *
   * @param path the path to validate
   * @return the validated path
   */
  @NotNull
  public static String validatePath(@NotNull String path) {
    return path.replace("/", File.separator);
  }

  /**
   * Get a file reader
   *
   * @param file the file that needs to be read
   * @return the file reader
   * @throws FileNotFoundException if the file is not found
   */
  @NotNull
  public static FileReader getReader(@NotNull File file) throws FileNotFoundException {
    return new FileReader(file);
  }

  /**
   * Get a directory or create it
   *
   * @param parent the path of the directory
   * @return the directory
   * @throws IOException if the directory cannot be created
   */
  @NotNull
  public static File directoryOrCreate(@NotNull String parent) throws IOException {
    File file = new File(validatePath(parent));
    if (!file.exists()) {
      if (file.mkdir()) {
        return file;
      } else {
        throw new IOException("Directory could not be created");
      }
    }
    if (file.isDirectory()) {
      return file;
    } else {
      throw new IOException("There's already a file with the name of the directory");
    }
  }

  /**
   * Copies a directory
   *
   * @param source the source directory
   * @param destination the destination directory
   * @throws IOException if the destination directory could not be created
   */
  public static void copyDirectory(@NotNull File source, @NotNull File destination)
      throws IOException {
    String[] list = source.list();
    if (source.isDirectory() && list != null) {
      if (!destination.exists()) {
        if (!destination.mkdir()) {
          throw new IOException(destination + " could not be created");
        }
      }
      for (String string : list) {
        File sourceFile = new File(source, string);
        File destinationFile = new File(destination, string);
        copyDirectory(sourceFile, destinationFile);
      }
    } else {
      copyFile(source, destination);
    }
  }

  /**
   * Copies a file
   *
   * @param source the source file
   * @param destination the destination file
   * @throws IOException if the files do not exist
   */
  public static void copyFile(@NotNull File source, @NotNull File destination) throws IOException {
    try (InputStream input = new FileInputStream(source);
        OutputStream output = new FileOutputStream(destination)) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = input.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }
    }
  }

  /** Validates the name/directory of a file to be compatible with every os */
  private static class FileNameValidator {

    /** The file */
    @NotNull private final File file;
    /** The directory of the file */
    @Nullable private File directory;

    /**
     * Starts the validation
     *
     * @param parent the directory of the file
     * @param fileName the file
     */
    public FileNameValidator(@Nullable String parent, @NotNull String fileName) {
      fileName = validatePath(fileName);
      this.file = parent == null ? new File(fileName) : new File(validatePath(parent), fileName);
      this.directory = file.getParentFile();
    }

    /**
     * Get the validated file
     *
     * @return the file
     */
    @NotNull
    public File getFile() {
      return file;
    }

    /**
     * Get the directory of the file
     *
     * @return the directory
     */
    @Nullable
    public File getDirectory() {
      return directory;
    }
  }
}
