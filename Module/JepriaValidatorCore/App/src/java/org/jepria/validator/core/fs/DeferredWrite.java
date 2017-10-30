package org.jepria.validator.core.fs;

import java.io.File;
import java.util.Objects;
import java.util.Stack;

/**
 * Отложенная запись в контекст.
 * Используется для изменения контекста после завершения валидации
 * (для поддержания персистентности контекста)
 */
// package
/*package*/abstract class DeferredWrite {
  
  private DeferredWrite() {}
  
  public abstract void perform();
  
  public static abstract class Deletion extends DeferredWrite {
    protected final File file;
    
    public Deletion(File file) {
      this.file = file;
    }
  }
  
  public static abstract class Renaming extends DeferredWrite {
    protected final File renameFrom, renameTo;
    
    public Renaming(File renameFrom, File renameTo) {
      this.renameFrom = renameFrom;
      this.renameTo = renameTo;
    }
  }
  
  public static class FileDeletion extends Deletion {
    public FileDeletion(File file) {
      super(file);
    }

    @Override
    public void perform() {
      file.delete();
    }
  }
  
  public static class DirectoryDeletion extends Deletion {
    public DirectoryDeletion(File directory) {
      super(directory);
    }

    @Override
    public void perform() {
      
      if (file == null || !file.isDirectory()) {
        return;
      } else if (!file.exists()) {
        // Не выбрасывать исключений, так как директория могла быть удалена в рамках рефакторинга.
        // Пример: delete(a/b/c); затем сразу delete(a/b/c/d).
        // TODO оптимизировать это (прямо во время добавления директорий в очередь 
        // на отложенное удаление из прикладного валидатора)
        return;
      }
      
      Stack<File> stack = new Stack<>();
      
      File directory;
      
      stack.push(file);
      while (!stack.isEmpty()) {
        directory = stack.pop();
        File[] children = directory.listFiles();
        if (children == null || children.length == 0) {
          directory.delete();
        } else {
          stack.push(directory);
          for (File child: children) {
            if (child.isDirectory()) {
              stack.push(child);
            } else {
              child.delete();
            }
          }
        }
      }
    }
    
    @Override
    // для использования в Set
    public int hashCode() {
      return Objects.hash(file);
    }
    
    @Override
    // для использования в Set
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || !(obj instanceof DirectoryDeletion)) {
        return false;
      }
      DirectoryDeletion directoryDeletion = (DirectoryDeletion)obj;
      
      return this.file == null && directoryDeletion.file == null ||
          this.file.getAbsolutePath() == null && directoryDeletion.file.getAbsolutePath() == null ||
          this.file.getAbsolutePath().equals(directoryDeletion.file.getAbsolutePath());
    }
  }
  
  public static class ResourceRenaming extends Renaming {

    public ResourceRenaming(File renameFrom, File renameTo) {
      super(renameFrom, renameTo);
    }

    @Override
    public void perform() {
      // Если целевой файл существует, затираем его.
      if (renameTo.exists()) {
        renameTo.delete();
      }

      // TODO может возникнуть ситуация, когда два правила сделали writeNewResource с одинаковым именем.
      // Тогда сохранится только последняя версия. Это проблема разработчика правил?
      
      File parent = renameTo.getParentFile();
      if (parent != null) {
        parent.mkdirs();
      }
      
      renameFrom.renameTo(renameTo);
    }
    
  }
  
  public static class DirectoryCreation extends DeferredWrite {

    private final File directory;
    
    public DirectoryCreation(File directory) {
      this.directory = directory;
    }
    
    @Override
    public void perform() {
      directory.mkdirs();
    }
    
  }
  
}
