package com.starfishst.util;

import com.starfishst.core.utils.files.CoreFiles;
import java.io.File;
import java.io.IOException;

public class CoreFilesTest {

  public static void main(String[] args) throws IOException {
    System.out.println(
        CoreFiles.directoryOrCreate(CoreFiles.currentDirectory() + "/dir1/dir2/dir3"));
    File file = CoreFiles.getOrCreate(CoreFiles.currentDirectory() + "/dir1/dir2/dir3", "test.txt");
    System.out.println(file);
  }
}
