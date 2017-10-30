package org.jepria.validator.core.fs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

public class FileWalker {
//  /** 
//   * Native implementation
//   */
//  public static void walk(File root, Consumer<? super File> action) {
//    if (root != null && action != null) {
//      Stack<File> stack = new Stack<>();
//      stack.push(root);
//      
//      while (!stack.isEmpty()) {
//        File file = stack.pop();
//        action.accept(file);
//        if (file.isDirectory()) {
//          File[] childs = file.listFiles();
//          if (childs != null && childs.length > 0) {
//            for (File child: childs) {
//              stack.push(child);
//            }
//          }
//        }
//      }
//    }
//  }
    
  //TODO удалить метод и класс, использовать java.nio.file.FileVisitor вместо этого
  public static void walk(File root, Consumer<? super File> action) {
    try {
      Files.walk(root.toPath())
          .forEach(path -> action.accept(path.toFile()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
