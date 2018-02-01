package org.jepria.validator.core.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jepria.validator.core.base.MessageCollector;
import org.jepria.validator.core.base.Validator;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.contextadapter.ContextReadAdapter;
import org.jepria.validator.core.base.contextadapter.ContextWriteAdapter;
import org.jepria.validator.core.base.exception.ResourceAlreadyExistsException;
import org.jepria.validator.core.base.exception.ResourceDoesNotExistException;
import org.jepria.validator.core.base.resource.Directory;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.Resource;

/**
 * Класс для запуска валидации в файловом контексте (где ресурсы для валидации &mdash; это файлы на диске)
 */
public class FileContextValidator extends Validator {

  /**
   * Корневая директория с файлами для валидации
   */
  private final File rootDir;
  
  /**
   * @param rootDir корневая директория с файлами для валидации
   */
  public FileContextValidator(File rootDir) {
    this.rootDir = rootDir;
    
    setContextReadAdapter(fileContextReadAdapter);
    setContextWriteAdapter(fileContextWriteAdapter);
  }
  
  
  public File getRootDir() {
    return rootDir;
  }
  
  /**
   * Преобразует файлы корневой директории в ресурсы
   *  
   * @param filesForValidation файлы в корневой директории, которые необходимо преобразовать в ресурсы. Если {@code null}, то преобразуются все файлы в директории
   * @return преобразованные файлы в виде списка ресурсов или пустой список, если файлов нет
   * 
   * @throws IllegalStateException если корневая директория не существует
   * @throws IllegalArgumentException если какой-либо файл из списка {@code filesForValidation} не принадлежит корневой директории
   */
  private List<Resource> collectResources(List<File> filesForValidation) {

    final List<Resource> resourcesForValidation = new ArrayList<>();
    
    if (filesForValidation != null) {
      // check the resources to be located within the rootDir
      filesForValidation.forEach(file -> {
        if (!file.getAbsolutePath().startsWith(rootDir.getAbsolutePath())) {
          throw new IllegalArgumentException("A file for validation is not located within the root directory");
        }
        
        // Доступ к ФС только через адаптер
        resourcesForValidation.add(fileContextReadAdapter.getPlainResource(convertToPathName(file)));
      });
    } else {
      // validate all resources
      
      // Доступ к ФС только через адаптер
      FileWalker.walk(rootDir, file -> {
          if (file.isFile()) {
            resourcesForValidation.add(fileContextReadAdapter.getPlainResource(convertToPathName(file)));
          } else if (file.isDirectory() && !rootDir.getAbsolutePath().equals(file.getAbsolutePath())) {
            resourcesForValidation.add(fileContextReadAdapter.getDirectory(convertToPathName(file)));
          }
        }
      );
    }
    
    return resourcesForValidation;
  }
  
  /**
   * Запуск валидации файлов на множестве правил
   * @param rules
   * @param filesForValidation файлы в корневой директории для валидации. Если {@code null}, то валидируется вся директория
   * @param validationMessageCollector
   * @param transform
   * @param transformMessageCollector
   */
  public void validate(
      List<ValidatorRule> rules,  
      List<File> filesForValidation, 
      MessageCollector validationMessageCollector,
      boolean transform,
      MessageCollector transformMessageCollector,
      ProgressCallback progressCallback) {
    
    if (rootDir == null || !rootDir.isDirectory()) {
      throw new IllegalStateException("A rootDir ["+rootDir+"] does not represent an existing directory");
    }
    
    if (rules == null || rules.isEmpty()) {
      return;
    }
    
    List<Resource> resourcesForValidation;
    
    if (!transform) {
      resourcesForValidation = collectResources(filesForValidation);
      
      
      if (progressCallback != null) {
        progressCallback.setTotal(resourcesForValidation.size() * rules.size());
        progressCallback.initialize();
      }
      
      
      validateInContext(
          rules,
          resourcesForValidation,
          validationMessageCollector,
          progressCallback);
    } else {
      
      boolean progressCallbackInitialized = false;
      
      for (ValidatorRule rule: rules) {
        // refill the list on each iteration!
        resourcesForValidation = collectResources(filesForValidation);
        
        
        // initialize progress callback at the first pass
        if (progressCallback != null && !progressCallbackInitialized) {
          progressCallback.setTotal(resourcesForValidation.size() * rules.size());
          progressCallback.initialize();
          progressCallbackInitialized = true;
        }
        
        
        validateUnderRule(rule,
            resourcesForValidation, 
            validationMessageCollector, 
            true, 
            transformMessageCollector, 
            progressCallback);
      }
    }
  }
  
  /**
   * Создает временный файл в Temp-директории (определяется системой)
   * 
   * @param tag символическая метка файла, которая будет присутствовать в имени времнного файла (только для удобства)
   * @return созданный временный файл
   */
  protected File createTempFile(String tag) {
    try {
      File tempFile = File.createTempFile(tag, ".jepria_validator_tmp");
      tempFile.deleteOnExit();
      return tempFile;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public class FileResource implements Resource {
    
    protected final File file;
    
    public FileResource(File file) {
      this.file = file;
    }
    
    @Override
    public String getName() {
      return file.getName();
    }

    @Override
    public String getPathName() {
      return convertToPathName(file);
    }
  }
  
  public class FilePlainResource extends FileResource implements PlainResource {
    
    public FilePlainResource(File file) {
      super(file);
    }
    
    @Override
    public InputStream newInputStream() {
      try {
        return new FileInputStream(file);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  public class FileDirectory extends FileResource implements Directory {
    
    /**
     * Вычисляется при первом обращении один раз (дерево ресурсов персистентно в рамках валидации по одному правилу)  
     */
    private Boolean containsResources = null;
    
    /**
     * Вычисляется при первом обращении один раз (дерево ресурсов персистентно в рамках валидации по одному правилу)  
     */
    private Boolean isEmpty = null;
    
    public FileDirectory(File file) {
      super(file);
    }
    
    @Override
    public boolean isEmpty() {
      if (isEmpty == null) {
        isEmpty = !containsResources() && fileContextReadAdapter.listDirectoriesByPathName(
            convertToPathName(file) + "/.+").isEmpty();
      }
      return isEmpty.booleanValue();
    }

    @Override
    public boolean containsResources() {
      if (containsResources == null) {
        containsResources = !fileContextReadAdapter.listPlainResourcesByPathName(
            convertToPathName(file) + "/.+").isEmpty();
      }
      return containsResources.booleanValue();
    }
  }

  /**
   * Преобразует файл, соответствующий ресурсу, в относительный путь
   * @param file соответствующий ресурсу файл во внешнем контексте
   * @return относительный путь ресурса в валидационном контексте
   */
  public String convertToPathName(File file) {
    // TODO в некотрорых случаях возникают эксепшены из relativize: 'other' is different type of path
    // (например, ели в CLI передать --directory в относительном, а не в абсолютном виде)
    return Paths.get(rootDir.getAbsolutePath()).relativize(file.toPath()).toString().replaceAll("\\\\", "/");
  }
  
  /**
   * Преобразует относительный путь ресурса в соответствующий ему файл
   * @param pathName относительный путь в валидационном контексте
   * @return соответствующий файл во внешнем контексте
   */
  public File convertToFile(String pathName) {
    return Paths.get(rootDir.getAbsolutePath(), pathName).toFile();
  }
  
  /**
   * Список для накопления отложенных записей в контекст (в рамках валидации по одному правилу)
   */
  private final List<DeferredWrite> deferredContextWrites = new ArrayList<>();
  
  @Override
  public void validateUnderRule(ValidatorRule rule, List<Resource> resourcesForValidation,
      MessageCollector validationMessageCollector, boolean transform, MessageCollector transformMessageCollector,
      ProgressCallback progressCallback) {
    
    deferredContextWrites.clear();
    
    super.validateUnderRule(rule, resourcesForValidation, validationMessageCollector, transform,
        transformMessageCollector, progressCallback);
    
    // Осуществим накопленные отложенные записи контекста
    for (DeferredWrite contextWriting: deferredContextWrites) {
      contextWriting.perform();
    }
  }
  
  
  
  private final FileContextReadAdapter fileContextReadAdapter = new FileContextReadAdapter();
  
  // inner class
  private class FileContextReadAdapter implements ContextReadAdapter {
    
    @Override
    public PlainResource getPlainResource(String pathName) {
      if (plainResourceExists(pathName)) {
        return new FilePlainResource(convertToFile(pathName));
      } else {
        throw new ResourceDoesNotExistException(pathName);
      }
    }
    
    @Override
    public Directory getDirectory(String pathName) {
      if (directoryExists(pathName)) {
        return new FileDirectory(convertToFile(pathName));
      } else {
        throw new ResourceDoesNotExistException(pathName);
      }
    }
    
    @Override
    public boolean plainResourceExists(String pathName) {
      File file = convertToFile(pathName);
      return file.exists() && !file.isDirectory();
    }
    
    @Override
    public boolean directoryExists(String pathName) {
      File dir = convertToFile(pathName);
      return dir.exists() && dir.isDirectory();
    }
    
    @Override
    public List<PlainResource> listPlainResourcesByName(String nameRegex) {
      return listPlainResourcesByPathName("(.*/)?" + nameRegex/*no Pattern.quote because regex*/);
    }
    
    @Override
    public List<PlainResource> listPlainResourcesByPathName(String pathNameRegex) {
      List<PlainResource> resources = new ArrayList<>();
      
      FileWalker.walk(rootDir, file -> {
        if (file.isFile() && convertToPathName(file).matches(pathNameRegex)) {
          resources.add(new FilePlainResource(file));
        }
      });
      
      return resources;
    }
    
    @Override
    public List<Directory> listDirectoriesByName(String nameRegex) {
      return listDirectoriesByPathName("(.*/)?" + nameRegex/*no Pattern.quote because regex*/);
    }
    
    @Override
    public List<Directory> listDirectoriesByPathName(String pathNameRegex) {
      List<Directory> directories = new ArrayList<>();
      
      FileWalker.walk(rootDir, file -> {
        if (file.isDirectory() && convertToPathName(file).matches(pathNameRegex)) {
          directories.add(new FileDirectory(file));
        }
      });
      
      return directories;
    }
  }
  
  
  
  private final FileContextWriteAdapter fileContextWriteAdapter = new FileContextWriteAdapter();
  
  // inner class
  private class FileContextWriteAdapter implements ContextWriteAdapter {
    
    @Override
    public void deleteResource(Resource resource) {
      File file = convertToFile(resource.getPathName());
      if (file != null && file.isDirectory()) {
        deferredContextWrites.add(new DeferredWrite.DirectoryDeletion(file));
      } else {
        deferredContextWrites.add(new DeferredWrite.FileDeletion(file));
      }
    }
    
    @Override
    public void renameResource(Resource resource, String newPathName) {
      File renameFromFile = convertToFile(resource.getPathName());
      File renameToFile = convertToFile(newPathName);
      deferredContextWrites.add(new DeferredWrite.ResourceRenaming(renameFromFile, renameToFile));
    }
    
    @Override
    public OutputStream newRefactoringStream(PlainResource resource) {
      File file = convertToFile(resource.getPathName());
      File tempFile = createTempFile(resource.getPathName().replaceAll("/|\\.", "_"));
      deferredContextWrites.add(new DeferredWrite.ResourceRenaming(tempFile, file));
      try {
        return new FileOutputStream(tempFile);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    
    @Override
    public OutputStream writeNewResource(String pathName) {
      if (fileContextReadAdapter.plainResourceExists(pathName)) {
        throw new ResourceAlreadyExistsException(pathName);
      }
      
      File newFile = convertToFile(pathName);
      File tempFile = createTempFile(pathName.replaceAll("/|\\.", "_"));
      deferredContextWrites.add(new DeferredWrite.ResourceRenaming(tempFile, newFile));
      try {
        return new FileOutputStream(tempFile);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    
    @Override
    public void createNewDirectory(String pathName) {
      if (fileContextReadAdapter.directoryExists(pathName)) {
        throw new ResourceAlreadyExistsException(pathName);
      }
      
      File newDirectory = convertToFile(pathName);
      deferredContextWrites.add(new DeferredWrite.DirectoryCreation(newDirectory));
    }
  }
}
