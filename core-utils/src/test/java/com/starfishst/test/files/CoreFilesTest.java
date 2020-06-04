package com.starfishst.test.files;

import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.files.CoreFiles;
import com.starfishst.utils.gson.GsonProvider;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/** Tests for {@link com.starfishst.core.utils.files.CoreFiles} */
public class CoreFilesTest {

  public static void main(String[] args) throws IOException {
    String[] strings = Lots.array("Hello", "How are you?!");
    File file = CoreFiles.getOrCreate(CoreFiles.currentDirectory() + "/tests/", "test.json");
    GsonProvider.save(file, strings);
    String uri = CoreFiles.currentDirectory() + "/tests/test.json";
    File get = CoreFiles.getOrCreate(uri);
    FileReader reader = new FileReader(get);
    System.out.println(Arrays.toString(GsonProvider.GSON.fromJson(reader, String[].class)));
    reader.close();

    CoreFiles.getFileOrResource(
        CoreFiles.currentDirectory(),
        "test-resource.json",
        CoreFiles.getResource("test-resource.json"));
    CoreFiles.getFileOrResource(
        CoreFiles.currentDirectory(),
        "test-resource2.json",
        CoreFiles.getResource("/dir1/dir2/test-resource2.json"));
  }
}
